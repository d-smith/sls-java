package com.ds;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class Reader implements RequestHandler<Map<String, String>, String> {

    private Consumer<String, DataRecord> consumer;
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
            props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, DataRecord.class);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-lambda");
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

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
        logger.log("reader invoked\n");


        logger.log("poll for records\n");
        ConsumerRecords<String,DataRecord> records = consumer.poll(Duration.ofMillis(10000));
        logger.log("poll finished - iterate through results\n");
        for(ConsumerRecord<String,DataRecord> record: records) {
            logger.log("format details...\n");
            String key = record.key();
            DataRecord value = record.value();
            logger.log("key is " + key + ", value is " + value + "\n");
        }

        return "OK";
    }
}
