package ng.kennie.aws.quiz.migration.dao;

import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.ANSWER;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.CATEGORY;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.CHOICES;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.DESC;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.NUM;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.TABLE_NAME_QUIZ_ENTITY;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.TITLE;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import ng.kennie.aws.quiz.migration.model.AWSQuizEntity;

@Service
public class AWSQuizDao extends DynamoDBDaoSupport {

	public static final String QUIZ_MAX_NUM = "quiz_max_num";

	public AWSQuizEntity readQuiz(final String category, final Long num) {
		final Table quizTable = getTable(TABLE_NAME_QUIZ_ENTITY);
		final Item item = quizTable.getItem(CATEGORY, category, NUM, num);

		if (item == null) {
			return null;
		}

		final AWSQuizEntity quiz = new AWSQuizEntity() //
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
}
