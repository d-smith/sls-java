package com.ds;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Reader implements RequestHandler<Map<String, String>, String> {


    public Reader() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find " + "config.properties");
                return;
            }

            props.load(input);

            // Add additional properties.
            props.put(ProducerConfig.ACKS_CONFIG, "all");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");


            props.keySet().forEach(x -> System.out.println(x));


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String handleRequest(Map<String, String> stringStringMap, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("reader invoked");
        return "OK";
    }
}
