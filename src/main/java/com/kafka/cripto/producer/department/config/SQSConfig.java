package com.kafka.cripto.producer.department.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableSqs
public class SQSConfig {

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.auth.accessKey}")
	private String accessKey;

	@Value("${aws.auth.secretKey}")
	private String secretKey;

	private AWSStaticCredentialsProvider credentialsProvider(){
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
	}

	@Bean
	@Primary
	public AmazonSQSAsync amazonSQSAsync() {
		return AmazonSQSAsyncClientBuilder.standard()
				.withCredentials(credentialsProvider())
				.withRegion(awsRegion)
				.build();
	}

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
		return new QueueMessagingTemplate(amazonSQSAsync());
	}


	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQSAsync) {
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		factory.setAmazonSqs(amazonSQSAsync);
		factory.setAutoStartup(true);
		factory.setMaxNumberOfMessages(10);
		factory.setTaskExecutor(createDefaultTaskExecutor());
		return factory;
	}

	protected AsyncTaskExecutor createDefaultTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("SQSExecutor - ");
		threadPoolTaskExecutor.setCorePoolSize(100);
		threadPoolTaskExecutor.setMaxPoolSize(100);
		threadPoolTaskExecutor.setQueueCapacity(2);
		threadPoolTaskExecutor.afterPropertiesSet();
		return threadPoolTaskExecutor;
	}

}