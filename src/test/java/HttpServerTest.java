/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Santiago
 */
public class HttpServerTest {

    
    private static final String URL = "http://localhost:35000/";

    @BeforeAll
    public static void startServer() {
        new Thread(() -> {
            try {
                HttpServer.runServer(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // darle tiempo a que levante el socket
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @AfterAll
    public static void stopServer() {
        try {
            HttpServer.stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileHtml()  {

        String file = "index.html";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileForm()  {

        String file = "form.html";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileCss()  {
   
        String file = "styles.css";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileJs()  {
        String file = "script.js";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticImagePNG()  {
        String file = "image.png";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadGreetingControllerWithQuery()  {
        String file = "app/greeting?name=Santiago";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("Hola Santiago", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadGreetingControllerWithoutQuery()  {
        String file = "app/greeting";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("Hola World", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
