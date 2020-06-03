package com.javatechie.cloud.stream.api;

import callBack.PeterCallback;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;
import java.util.UUID;

@SpringBootApplication
@EnableBinding(Source.class)
@RestController
public class SpringCloudStreamPublisherApplication {

    @Autowired
    private MessageChannel output;

    @PostMapping("/publish")
    public Book publishEvent(@RequestBody Book book) {
        output.send(MessageBuilder.withPayload(book).build());
        publish();
        return book;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamPublisherApplication.class, args);
        publish();
    }

    private static Properties setProducerProperty(){
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("ack",1);
        producerProps.put("retries", 1);
        /*
            배치사이즈가 작으면 대량전송시 유실됨 최소 1000이상 잡는게 효율적
        */
        producerProps.put("batch.size", 1);
        producerProps.put("linger.ms", 1);
        producerProps.put("buffer.memory", 24568545);
        return producerProps;
    }

    private static void publish(){
        Logger logger = LoggerFactory.getLogger(SpringCloudStreamPublisherApplication.class);

        KafkaProducer<String, String> producer = new KafkaProducer(setProducerProperty());

        logger.info("Pubulish Start");
        for (int i=0; i<10; i++) {
//            ProducerRecord<String, String> data = new ProducerRecord<String, String>;
            producer.send(new ProducerRecord<String, String>("file", String.format("%d",i)));
            logger.info(String.format("%d",i));


        }
        producer.close();
        logger.info("Pubulish End");
    }

}
