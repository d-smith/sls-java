package com.ds;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.Producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import java.util.concurrent.ExecutionException;
import org.apache.kafka.common.errors.TopicExistsException;



// Handler value: example.Handler
public class Handler implements RequestHandler<Map<String,String>, String>{
  
  public static void createTopic(final String topic,
                          final Properties cloudConfig) {
      final NewTopic newTopic = new NewTopic(topic, Optional.empty(), Optional.empty());
      try (final AdminClient adminClient = AdminClient.create(cloudConfig)) {
          adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
      } catch (final InterruptedException | ExecutionException e) {
          // Ignore if TopicExistsException, which may be valid if topic exists
          if (!(e.getCause() instanceof TopicExistsException)) {
              throw new RuntimeException(e);
          }
      }
  }
  
  public Handler() {
    System.out.println("It is constructed...");
    
    
     try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
       Properties props = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find " + "config.properties");
                return;
            }

            props.load(input);
            props.keySet().forEach(x -> System.out.println(x));
            
            createTopic("sample", props);
            
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