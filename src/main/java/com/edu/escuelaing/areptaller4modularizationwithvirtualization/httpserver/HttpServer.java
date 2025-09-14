package com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver;

import com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.anotations.RestController;
import com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.anotations.GetMapping;
import com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.anotations.RequestParam;

import java.net.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author santiago.gualdron-r
 */
public class HttpServer {
    
    public static Map<String, Method> services = new HashMap<>();
    private static final String directory = "src/main/java/com/edu/escuelaing/ArepTaller4modularizationWithVirtualization/httpserver/resources";
    private static volatile boolean running = true;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void loadServices() throws ClassNotFoundException{
        try{
            Class c = Class.forName("com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.anotations.GreetingController");
            
            if (c.isAnnotationPresent(RestController.class)){
                Method[] methods = c.getDeclaredMethods();
                for (Method m : methods){
                    if (m.isAnnotationPresent(GetMapping.class)){
                        String mapping = m.getAnnotation(GetMapping.class).value();
                        services.put(mapping, m);
                    }
                }
            }
        }catch(ClassNotFoundException ex){
        
        }
    }
    
    public static void runServer(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
        loadServices();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;

        boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                clientSocket.getInputStream()));
                String inputLine, outputLine;

                String path = null;
                boolean firstline = true;
                URI requri = null;

                while ((inputLine = in.readLine()) != null) {
                    if (firstline) {
                        requri = new URI(inputLine.split(" ")[1]);
                        System.out.println("Path: " + requri.getPath());
                        firstline = false;
                    }
                    System.out.println("Received: " + inputLine);
                    if (!in.ready()) {
                        break;
                    }
                }

                if (requri.getPath().startsWith("/app")) {
                    outputLine = invokeService(requri, out);
                    out.println(outputLine);
                } else {
                    // Pasamos PrintWriter y OutputStream
                    serveStaticFile(requri.getPath(), out, clientSocket.getOutputStream());
                }

                out.close();
                in.close();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void stopServer() {
        running = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        System.out.println("Servidor detenido.");
    }

    private static String helloService(URI requesturi) {
        //Encabezado con content-type adaptado para retornar un JSON        
        String response = "HTTP/1.1 200 OK\r\n"
                + "content-type: application/json\r\n"
                + "\r\n";
        //Extrae el valor de "name" desde el query.
        String name = requesturi.getQuery().split("=")[1]; //name=jhon

        //Crea la respuesta completa                
        response = response + "{\"mensaje\": \"Hola " + name + "\"}";
        return response;
    }

    private static void serveStaticFile(String path, PrintWriter out, OutputStream rawOut) {
        if (path.equals("/")) {
            path = "/index.html";
        }

        File file = new File(directory + path);
        if (!file.exists() || file.isDirectory()) {
            out.println(error404());
            return;
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String contentType = getContentType(path);

            // --- Cabecera HTTP ---
            String header = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n" +
                    "\r\n";

            // --- Si es imagen/binario ---
            if (contentType.startsWith("image/")) {
                rawOut.write(header.getBytes());
                rawOut.write(fileContent);
                rawOut.flush();
            } else {
                // --- Si es texto (html, css, js, etc.) ---
                out.println(header);
                out.println(new String(fileContent));
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            out.println(error500());
        }
    }

    private static String invokeService(URI requri, PrintWriter out) {
        String header = "HTTP/1.1 200 OK\r\n"
                + "content-type: text/html\r\n"
                + "\r\n";
        try{
            HttpRequest req = new HttpRequest(requri);
            HttpResponse res = new HttpResponse(out);
            String servicePath = requri.getPath().substring(4);
            Method m = services.get(servicePath);
            
            String[] argsValues;
            RequestParam rp = (RequestParam) m.getParameterAnnotations()[0][0];
            if(requri.getQuery() == null){
                argsValues = new String[]{rp.defaultValue()};
            }else{
                String queryParamName = rp.value();
                argsValues = new String[]{req.getValues(queryParamName)};
            }
            return header + m.invoke(null, (Object[]) argsValues);
        }catch(IllegalAccessException ex){
            System.getLogger(HttpServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }catch (InvocationTargetException ex){
            System.getLogger(HttpServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return header + "Error!";
    }

    public static void staticfiles(String localFilesPath) {
    }
    
    private static String error404() {
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<h1>404 Not Found</h1>";
    }
    
    private static String error500() {
        return "HTTP/1.1 500 Internal Server Error\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<h1>500 Internal Server Error</h1>";
    }

    private static String getContentType(String fileName) {
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }
}
