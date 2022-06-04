package com.kafka.cripto.producer.department.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KmsConfig {

	@Value("${aws.kms.arn.sales}")
	private String kmsArnSales;

	@Value("${aws.kms.arn.finance}")
	private String kmsArnFinance;

	@Value("${aws.auth.accessKey}")
	private String accessKey;

	@Value("${aws.auth.secretKey}")
	private String secretKey;

	private KmsMasterKeyProvider keyProviderSales;

	private KmsMasterKeyProvider keyProviderFinance;

	private AWSStaticCredentialsProvider credentialsProvider(){
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
	}

	@Bean
	public KmsMasterKeyProvider keyProviderFinance(){
		if(keyProviderFinance == null){
			this.keyProviderFinance = KmsMasterKeyProvider.builder()
					.withCredentials(credentialsProvider())
					.buildStrict(kmsArnFinance);
		}
		return this.keyProviderFinance;
	}

	@Bean
	public KmsMasterKeyProvider keyProviderSales(){
		if(keyProviderSales == null){
			this.keyProviderSales = KmsMasterKeyProvider.builder()
					.withCredentials(credentialsProvider())
					.buildStrict(kmsArnSales);
		}
		return this.keyProviderSales;
	}

}