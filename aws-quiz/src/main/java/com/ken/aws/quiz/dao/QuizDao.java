package com.ken.aws.quiz.dao;

import static com.ken.aws.quiz.model.QuizControl.CONTROL_FIELD;
import static com.ken.aws.quiz.model.QuizControl.MAX_NUM;
import static com.ken.aws.quiz.model.QuizControl.MY_CHECK;
import static com.ken.aws.quiz.model.QuizControl.TABLE_NAME_QUIZ_CONTROL;
import static com.ken.aws.quiz.model.QuizEntity.ANSWER;
import static com.ken.aws.quiz.model.QuizEntity.CATEGORY;
import static com.ken.aws.quiz.model.QuizEntity.CHOICES;
import static com.ken.aws.quiz.model.QuizEntity.DESC;
import static com.ken.aws.quiz.model.QuizEntity.NUM;
import static com.ken.aws.quiz.model.QuizEntity.TABLE_NAME_QUIZ_ENTITY;
import static com.ken.aws.quiz.model.QuizEntity.TITLE;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.ken.aws.quiz.model.QuizEntity;

@Service
public class QuizDao extends DynamoDBDaoSupport {

	public static final String QUIZ_MAX_NUM = "quiz_max_num";

	public QuizEntity readQuiz(String category, Long num) {
		Table quizTable = getTable(TABLE_NAME_QUIZ_ENTITY);
		Item item = quizTable.getItem(CATEGORY, category, NUM, num);

		if (item == null) {
			return null;
		}

		QuizEntity quiz = new QuizEntity() //
				.category(category) //
				.num(num) //
				.title(item.getString(TITLE)) //
				.choices(item.getString(CHOICES)) //
				.answer(item.getString(ANSWER));

		if (item.isPresent(DESC)) {
			quiz.desc(item.getString(DESC));
		}

		return quiz;
	}

	public Long getMaxNum(String category) {
		Table quizControl = getTable(TABLE_NAME_QUIZ_CONTROL);
		final KeyAttribute keyAttribute = new KeyAttribute(CONTROL_FIELD, category);
		Item item = quizControl.getItem(keyAttribute);

		long maxNum = item.getLong(MAX_NUM);

		return maxNum;
	}

	public Long getMyCheck(String category) {
		Table quizControl = getTable(TABLE_NAME_QUIZ_CONTROL);
		final KeyAttribute keyAttribute = new KeyAttribute(CONTROL_FIELD, category);
		Item item = quizControl.getItem(keyAttribute);

		long maxNum = item.getLong(MY_CHECK);

		return maxNum;
	}

	public void addQuiz(QuizEntity quiz) {
		final Table quizControlTable = getTable(TABLE_NAME_QUIZ_CONTROL);

		final KeyAttribute primaryKey = new KeyAttribute(CONTROL_FIELD, quiz.getCategory());
		final Map<String, String> nameMap = new NameMap().with("#update_param", MY_CHECK);
		final Map<String, Object> valueMap = new ValueMap().with(":update_value", quiz.getNum());

		final UpdateItemSpec updateItemSpec = new UpdateItemSpec() //
				.withPrimaryKey(primaryKey) //
				.withUpdateExpression("set #update_param = :update_value") //
				.withNameMap(nameMap) //
				.withValueMap(valueMap);

		quizControlTable.updateItem(updateItemSpec);

		final Table quizTable = getTable(TABLE_NAME_QUIZ_ENTITY);
		final Item quizItem = new Item() //
				.withPrimaryKey(CATEGORY, quiz.getCategory(), NUM, quiz.getNum()) //
				.with(TITLE, quiz.getTitle()) //
				.with(CHOICES, quiz.getChoices()) //
				.with(ANSWER, quiz.getAnswer());

		String desc = quiz.getDesc();
		if (desc != null && !"".equals(desc.trim())) {
			quizItem.with(DESC, quiz.getDesc());
		}

		quizTable.putItem(quizItem);
	}
}
