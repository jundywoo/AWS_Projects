package com.ken.aws.quiz.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
public class QuizConfiguration {

	private static final Log LOG = LogFactory.getLog(QuizConfiguration.class);

	private final String accessKey = "AKIAJ76L47KADFFC2YUQ";
	private final File secretKeyPath = new File(new File(System.getProperty("user.home"), "aws-db-key"), "key");

	@Bean
	public DynamoDB dynamoDB() throws IOException {
		final String secretKey;
		try (final BufferedReader reader = new BufferedReader(new FileReader(secretKeyPath))) {
			secretKey = reader.readLine();
		} catch (final IOException e) {
			throw e;
		}
		final AmazonDynamoDBClient client = new AmazonDynamoDBClient(new BasicAWSCredentials(accessKey, secretKey));

		Region region = Regions.getCurrentRegion();
		if (region == null) {
			region = Region.getRegion(Regions.AP_NORTHEAST_1);
			LOG.info("Region NOT found, default Tokyo configured.");
		}

		client.setRegion(region);
		final DynamoDB dynamoDB = new DynamoDB(client);

		return dynamoDB;
	}

}
