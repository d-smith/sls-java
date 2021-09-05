package com.ds;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class Reader implements RequestHandler<Map<String, String>, String> {

    private Consumer<Long, String> consumer;
    private static final String TOPIC = "sample";

    public Reader() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find " + "config.properties");
                return;
            }

            props.load(input);

            // Add additional properties.
            //props.put(ProducerConfig.ACKS_CONFIG, "all");

            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");

            props.keySet().forEach(x -> System.out.println(x));

            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList(TOPIC));

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
