/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author santiago.gualdron-r
 */
public class HttpRequest {
    private String path;
    private String query;
    private Map<String, String> queryParams;


    public HttpRequest(URI uri) {
        this.path = uri.getPath();
        this.query = uri.getQuery();
        this.queryParams = parseQueryParams(this.query);
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    public String getPath() {
        return path;
    }

    public String getValues(String key) {
        return queryParams.getOrDefault(key, null);
    }
}
