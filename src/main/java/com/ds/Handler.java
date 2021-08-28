package com.ds;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String>{
  
  public Handler() {
    System.out.println("It is constructed...");
    
    
     try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
       Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find " + "config.properties");
                return;
            }

            prop.load(input);
            prop.keySet().forEach(x -> System.out.println(x));
     } catch (IOException ex) {
            ex.printStackTrace();
        }
    
    
  }
  
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(Map<String,String> event, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String response = new String("200 OK");
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());
    return response;
  }
}