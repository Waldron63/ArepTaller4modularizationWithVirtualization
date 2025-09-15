package com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 *
 * @author santiago.gualdron-r
 */
public class MainStart {
    private static String staticFilesDirectory = "src/main/resources";
    
    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException{
        System.out.println("Starting MicroSpringBoot!");
        staticfiles("src/main/resources");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el servidor...");
            HttpServer.stopServer();
        }));
        HttpServer.runServer(args);
    }

    /**
     * Define el directorio donde se encuentran los archivos estáticos.
     * @param directory El directorio de archivos estáticos.
     */
    public static void staticfiles(String directory) {
        HttpServer.class.getClassLoader().getResource("path");
        if (System.getenv("DOCKER_ENV") != null) {
            staticFilesDirectory = "/usrapp/bin/resources"; // Ruta Docker
            System.out.println("Usando ruta Docker: " + staticFilesDirectory);
        } else {
            staticFilesDirectory = directory; // Ruta en local
            System.out.println("Usando ruta local: " + staticFilesDirectory);
        }
    }

    public static String getStaticFilesDirectory() {
        return staticFilesDirectory;
    }

}
