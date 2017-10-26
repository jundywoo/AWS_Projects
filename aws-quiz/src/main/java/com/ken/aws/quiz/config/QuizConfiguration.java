package com.ken.aws.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
public class QuizConfiguration {

	@Bean
	public DynamoDB dynamoDB() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Regions.getCurrentRegion());
		DynamoDB dynamoDB = new DynamoDB(client);

		return dynamoDB;
	}
}
