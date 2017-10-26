package com.ken.aws.quiz.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.ken.aws.quiz.model.Quiz;
import com.ken.aws.quiz.model.QuizControl;

@Service
public class QuizDao implements InitializingBean {

	public static final String TABLE_NAME_QUIZ = "quiz";
	public static final String TABLE_NAME_QUIZ_CONTROL = "quiz_control";
	public static final String QUIZ_MAX_NUM = "quiz_max_num";

	@Autowired
	private DynamoDB dynamoDB;

	@Override
	public void afterPropertiesSet() throws Exception {
		assert dynamoDB != null : "Dynamo DB service not found.";
	}

	public Quiz readQuiz(Long num) {
		// Entity entity = datastore.get(keyFactory.newKey(num)); // Load an Entity for
		// Key(id)
		//
		// if (entity == null) {
		// return null;
		// }
		//
		// Quiz quiz = new Quiz() //
		// .num(entity.getLong(Quiz.NUM)) //
		// .title(entity.getString(Quiz.TITLE)) //
		// .choices(entity.getString(Quiz.CHOICES)) //
		// .answer(entity.getString(Quiz.ANSWER));
		//
		// if (entity.contains(Quiz.DESC)) {
		// quiz.desc(entity.getString(Quiz.DESC));
		// }
		//
		// return quiz;
		return new Quiz();
	}

	public Long maxNum() {
		Table quizControl = dynamoDB.getTable(TABLE_NAME_QUIZ_CONTROL);
		final KeyAttribute keyAttribute = new KeyAttribute(QuizControl.CONTROL_FIELD, QUIZ_MAX_NUM);
		Item item = quizControl.getItem(keyAttribute);

		long maxNum = item.getLong(QuizControl.VALUE);

		return maxNum;
	}

	public void addQuiz(Quiz quiz) {
		// IncompleteKey key = keyFactory.newKey(quiz.getNum()); // Key will be assigned
		// once written
		// FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the
		// Entity
		// .set(Quiz.NUM, quiz.getNum()) //
		// .set(Quiz.TITLE, quiz.getTitle()) //
		// .set(Quiz.DESC, MyValueUtils.noIndexString(quiz.getDesc())) //
		// .set(Quiz.CHOICES, MyValueUtils.noIndexString(quiz.getChoices())) //
		// .set(Quiz.ANSWER, MyValueUtils.noIndexString(quiz.getAnswer())).build();
		// Entity addedEntity = datastore.add(entity); // Save the Entity

	}
}
