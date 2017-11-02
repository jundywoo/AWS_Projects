package com.ken.aws.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.ken.aws.quiz.model.QuizEntity;

public class QuizTableMigration {

	private static final Log LOG = LogFactory.getLog(QuizTableMigration.class);

	public static void main(String[] args) {
		// AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		//
		// Region region = Regions.getCurrentRegion();
		// if (region == null) {
		// region = Region.getRegion(Regions.AP_SOUTHEAST_1);
		// LOG.info("Region NOT found, default Singapore configured.");
		// }
		//
		// client.setRegion(region);
		// DynamoDB dynamoDB = new DynamoDB(client);
		//
		// for (String category : new String[] { "saa" }) {
		// Object[] keys = new Object[50];
		//
		// for (int i = 101; i <= 150; i++) {
		// final String quizId = category + "-" + i;
		// keys[i - 1 - 100] = quizId;
		// }
		// TableKeysAndAttributes quizTableKeysAndAttributes = new
		// TableKeysAndAttributes("quiz");
		// quizTableKeysAndAttributes.addHashOnlyPrimaryKeys(QuizEntity.QUIZ_ID, keys);
		//
		// List<Item> itemsToPut = getQuizItems(dynamoDB, 50,
		// quizTableKeysAndAttributes);
		//
		// int size = itemsToPut.size();
		// int batch = size / 25 - (size % 25 == 0 ? 1 : 0);
		// for (int i = 0; i <= batch; i++) {
		// int firstIndex = i * 25;
		// int batchSize = i < batch ? 25 : size - (i * 25);
		// int lastIndex = i * 25 + batchSize;
		// List<Item> itemsToPutByBatch = new ArrayList<>(batchSize);
		// for (int j = firstIndex; j < lastIndex; j++) {
		// itemsToPutByBatch.add(itemsToPut.get(j));
		// }
		// writeQuizItems(dynamoDB, itemsToPutByBatch);
		// }
		//
		// }
	}

	@SuppressWarnings("unused")
	private static void writeQuizItems(DynamoDB dynamoDB, List<Item> itemsToPut) {
		TableWriteItems quizTableWriteItems = new TableWriteItems(QuizEntity.TABLE_NAME_QUIZ_ENTITY);
		quizTableWriteItems.withItemsToPut(itemsToPut);

		BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(quizTableWriteItems);
		Map<String, List<WriteRequest>> unprocessedItems = null;
		do {
			unprocessedItems = outcome.getUnprocessedItems();

			if (!unprocessedItems.isEmpty()) {
				LOG.info("Writing the unprocessed items");
				outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
			}

		} while (!unprocessedItems.isEmpty());
	}

	@SuppressWarnings("unused")
	private static List<Item> getQuizItems(DynamoDB dynamoDB, int maxNum,
			TableKeysAndAttributes quizTableKeysAndAttributes) {
		BatchGetItemOutcome outcome = dynamoDB.batchGetItem(quizTableKeysAndAttributes);
		Map<String, KeysAndAttributes> unprocessed = null;
		List<Item> itemsToPut = new ArrayList<>(maxNum);
		// do {
		// for (Entry<String, List<Item>> tableItems :
		// outcome.getTableItems().entrySet()) {
		// if ("quiz".equals(tableItems.getKey())) {
		// List<Item> items = tableItems.getValue();
		// for (Item item : items) {
		// String quizId = item.getString(QuizEntity.QUIZ_ID);
		// int pos = quizId.indexOf('-');
		// if (!item.isPresent(QuizEntity.NUM)) {
		// item.with(QuizEntity.NUM, quizId.substring(pos + 1, quizId.length()));
		// }
		// if (!item.isPresent(QuizEntity.CATEGORY)) {
		// item.with(QuizEntity.CATEGORY, quizId.substring(0, pos));
		// }
		// item.removeAttribute(QuizEntity.QUIZ_ID);
		// itemsToPut.add(item);
		// }
		// }
		// }
		// unprocessed = outcome.getUnprocessedKeys();
		//
		// if (!unprocessed.isEmpty()) {
		// LOG.info("Retrieving the unprocessed keys");
		// outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
		// }
		//
		// } while (!unprocessed.isEmpty());

		return itemsToPut;
	}

}
