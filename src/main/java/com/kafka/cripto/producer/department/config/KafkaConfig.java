package com.kafka.cripto.producer.department.config;

import com.kafka.cripto.producer.department.model.Data;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Value("${kafka.producer.bootstrap.server}")
	private String bootstrapServer;

	@Bean
	public ProducerFactory producerFactory(){
		Map<String,Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	/**
	 * kafka-console-producer \
	 *   --topic <topic name> \
	 *   --bootstrap-server <server endpoint> \
	 *   --property parse.key=true \
	 *   --property key.separator="<key separator. ex: |>" \
	 *   --partition <partition number>
	 * @return
	 */
	@Bean
	public KafkaTemplate<String, Data> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}

}