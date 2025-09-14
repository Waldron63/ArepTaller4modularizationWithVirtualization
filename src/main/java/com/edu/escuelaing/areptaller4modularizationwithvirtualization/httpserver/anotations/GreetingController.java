/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.anotations;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author santiago.gualdron-r
 */
@RestController
@RequestMapping("/App")
public class GreetingController {
    private static final String template =  "Hello, %s!";
    private  final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
	return "Hola " + name;
    }
}
