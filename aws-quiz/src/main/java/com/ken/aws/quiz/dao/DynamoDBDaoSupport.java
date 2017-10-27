package com.ken.aws.quiz.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DynamoDBDaoSupport implements InitializingBean {

	@Autowired
	private DynamoDB dynamoDB;

	@Override
	public final void afterPropertiesSet() throws Exception {
		assert dynamoDB != null : "Dynamo DB service not found.";

		init();
	}

	protected void init() {

	}

	protected final Table getTable(String tableName) {
		return dynamoDB.getTable(tableName);
	}
}
