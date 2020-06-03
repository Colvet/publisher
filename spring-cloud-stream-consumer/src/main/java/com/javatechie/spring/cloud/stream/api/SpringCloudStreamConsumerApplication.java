package com.javatechie.spring.cloud.stream.api;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
//@EnableBinding(Sink.class)
public class SpringCloudStreamConsumerApplication {

    Logger logger = LoggerFactory.getLogger(SpringCloudStreamConsumerApplication.class);

//    @StreamListener("input")
//    public void consumeMessage(Book book) {
//        logger.info("Consume payload : " + book);
//        logger.info("book id :" + book.getId());
//    }


    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamConsumerApplication.class, args);
        Consume();
    }

    private static Properties setConsumerProperty(){
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("group.id", "file"); //
        consumerProps.put("enable.auto.commit", "true");
        consumerProps.put("auto.offset.reset", "latest");

        // 메시지와 키와 값에 문자열을 사용하면 내장된 StringDeserializer를 지정하면 된다.
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return consumerProps;
    }

    private static void Consume(){
        Logger logger = LoggerFactory.getLogger(SpringCloudStreamConsumerApplication.class);

        KafkaConsumer<String, String> consumer = new KafkaConsumer(setConsumerProperty());
        consumer.subscribe(Arrays.asList("file"));
        try{
            while(true) {

                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
//                    logger.info("Topic: %s, Partition: %s, Offset: %d, Key: %s, Value: %s\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    logger.info("\n" + record.getClass());
                }

            }
        }finally {
            consumer.close();
        }

    }


}
