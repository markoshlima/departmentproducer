package com.kafka.cripto.producer.department.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.cripto.producer.department.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SQSService {

    @Value("${kafka.producer.topic}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Data> kafkaTemplate;

    @Autowired
    private KmsService kmsService;

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();

    @SqsListener(value = "${aws.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void processMessage(String message) {
        try {
            System.out.println("--------------------------");
            System.out.println("Received new SQS message:" + message );
            Data data = OBJECT_MAPPER.readValue(message, Data.class);
            System.out.println("Sending to topic: "+topicName);
            data.setData(kmsService.encryptData(data.getData(), data.getDepartment()));
            //kafkaTemplate.send(topicName, data); // without kafka partition strategy by department
            kafkaTemplate.send(topicName, data.getDepartment(), data); //with kafka partition strategy by department
            System.out.println("Message published: "+data.toString());
        } catch (Exception e) {
            throw new RuntimeException("Cannot process message from SQS", e);
        }
    }

}