package com.ken.aws.quiz;

public class QuizTableMigration {

	public static void main(String[] args) {
		/*
		 * final Log LOG = LogFactory.getLog(QuizTableMigration.class);
		 * 
		 * AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		 * 
		 * Region region = Regions.getCurrentRegion(); if (region == null) { region =
		 * Region.getRegion(Regions.AP_SOUTHEAST_1);
		 * LOG.info("Region NOT found, default Singapore configured."); }
		 * 
		 * client.setRegion(region); DynamoDB dynamoDB = new DynamoDB(client); Table
		 * tableSaaQuiz = dynamoDB.getTable("saa-quiz"); Table tableQuiz =
		 * dynamoDB.getTable("quiz");
		 * 
		 * for (int i = 1; i <= 38; i++) { final KeyAttribute keyAttribute = new
		 * KeyAttribute(Quiz.NUM, i); Item item = tableSaaQuiz.getItem(keyAttribute);
		 * 
		 * if (item == null) { LOG.warn("SAA Quiz num=" + i + " not found!"); }
		 * 
		 * Quiz quiz = new Quiz() // .num(item.getLong(Quiz.NUM)) //
		 * .title(item.getString(Quiz.TITLE)) // .choices(item.getString(Quiz.CHOICES))
		 * // .answer(item.getString(Quiz.ANSWER));
		 * 
		 * if (item.isPresent(Quiz.DESC)) { quiz.desc(item.getString(Quiz.DESC)); }
		 * 
		 * final Item quizItem = new Item() // .withString(Quiz.QUIZ_ID, "saa-" +
		 * quiz.getNum()) // .withString(Quiz.TITLE, quiz.getTitle()) //
		 * .withString(Quiz.CHOICES, quiz.getChoices()) // .withString(Quiz.ANSWER,
		 * quiz.getAnswer());
		 * 
		 * String desc = quiz.getDesc(); if (desc != null && !"".equals(desc.trim())) {
		 * quizItem.withString(Quiz.DESC, quiz.getDesc()); }
		 * 
		 * tableQuiz.putItem(quizItem); }
		 */
	}

}
