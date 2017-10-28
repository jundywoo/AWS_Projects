package com.ken.aws.quiz.dao;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.ken.aws.quiz.model.Quiz;
import com.ken.aws.quiz.model.QuizControl;

@Service
public class QuizDao extends DynamoDBDaoSupport {

	public static final String TABLE_NAME_QUIZ = "quiz";
	public static final String TABLE_NAME_QUIZ_CONTROL = "quiz_control";
	public static final String QUIZ_MAX_NUM = "quiz_max_num";

	public Quiz readQuiz(String quizId) {
		Table quizTable = getTable(TABLE_NAME_QUIZ);
		final KeyAttribute keyAttribute = new KeyAttribute(Quiz.QUIZ_ID, quizId);
		Item item = quizTable.getItem(keyAttribute);

		if (item == null) {
			return null;
		}

		Quiz quiz = new Quiz() //
				.quizId(item.getString(Quiz.QUIZ_ID)) //
				.title(item.getString(Quiz.TITLE)) //
				.choices(item.getString(Quiz.CHOICES)) //
				.answer(item.getString(Quiz.ANSWER));

		if (item.isPresent(Quiz.DESC)) {
			quiz.desc(item.getString(Quiz.DESC));
		}

		return quiz;
	}

	public Long maxNum(String category) {
		Table quizControl = getTable(TABLE_NAME_QUIZ_CONTROL);
		final KeyAttribute keyAttribute = new KeyAttribute(QuizControl.CONTROL_FIELD, category + "-" + QUIZ_MAX_NUM);
		Item item = quizControl.getItem(keyAttribute);

		long maxNum = item.getLong(QuizControl.VALUE);

		return maxNum;
	}

	public void addQuiz(Quiz quiz, String category, Long num) {
		final Table quizControlTable = getTable(TABLE_NAME_QUIZ_CONTROL);

		final KeyAttribute primaryKey = new KeyAttribute(QuizControl.CONTROL_FIELD, category + "-" + QUIZ_MAX_NUM);
		final Map<String, String> nameMap = new NameMap().with("#update_param", QuizControl.VALUE);
		final Map<String, Object> valueMap = new ValueMap().with(":update_value", num);

		final UpdateItemSpec updateItemSpec = new UpdateItemSpec() //
				.withPrimaryKey(primaryKey) //
				.withUpdateExpression("set #update_param = :update_value") //
				.withNameMap(nameMap) //
				.withValueMap(valueMap);

		quizControlTable.updateItem(updateItemSpec);

		final Table quizTable = getTable(TABLE_NAME_QUIZ);
		final Item quizItem = new Item() //
				.with(Quiz.QUIZ_ID, quiz.getQuizId()) //
				.with(Quiz.TITLE, quiz.getTitle()) //
				.with(Quiz.CHOICES, quiz.getChoices()) //
				.with(Quiz.ANSWER, quiz.getAnswer());

		String desc = quiz.getDesc();
		if (desc != null && !"".equals(desc.trim())) {
			quizItem.with(Quiz.DESC, quiz.getDesc());
		}

		quizTable.putItem(quizItem);
	}
}
