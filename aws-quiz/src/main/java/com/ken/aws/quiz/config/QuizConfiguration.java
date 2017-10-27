package com.ken.aws.quiz.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
public class QuizConfiguration {

	private static final Log LOG = LogFactory.getLog(QuizConfiguration.class);

	@Bean
	public DynamoDB dynamoDB() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();

		Region region = Regions.getCurrentRegion();
		if (region == null) {
			region = Region.getRegion(Regions.AP_SOUTHEAST_1);
			LOG.info("Region NOT found, default Singapore configured.");
		}

		client.setRegion(region);
		DynamoDB dynamoDB = new DynamoDB(client);

		return dynamoDB;
	}
}
