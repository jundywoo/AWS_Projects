package ng.kennie.aws.quiz.migration.dao;

import static ng.kennie.aws.quiz.migration.model.AWSQuizComment.AUTHOR;
import static ng.kennie.aws.quiz.migration.model.AWSQuizComment.COMMENT;
import static ng.kennie.aws.quiz.migration.model.AWSQuizComment.DATE;
import static ng.kennie.aws.quiz.migration.model.AWSQuizComment.QUIZ_ID;
import static ng.kennie.aws.quiz.migration.model.AWSQuizComment.TABLE_NAME_QUIZ_COMMENT;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.ANSWER;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.CATEGORY;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.CHOICES;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.DESC;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.NUM;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.TABLE_NAME_QUIZ_ENTITY;
import static ng.kennie.aws.quiz.migration.model.AWSQuizEntity.TITLE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import ng.kennie.aws.quiz.migration.model.AWSQuizComment;
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

	public List<AWSQuizComment> readCommenByQuiz(final String quizId) {
		final List<AWSQuizComment> quizComments = new ArrayList<>();

		final Table table = getTable(TABLE_NAME_QUIZ_COMMENT);
		final QuerySpec spec = new QuerySpec() //
				.withKeyConditionExpression(QUIZ_ID + " = :value_num") //
				.withValueMap(new ValueMap().with(":value_num", quizId));

		final ItemCollection<QueryOutcome> items = table.query(spec);

		for (final Item item : items) {
			final AWSQuizComment comment = new AWSQuizComment() //
					.quizId(item.getString(QUIZ_ID)) //
					.author(item.getString(AUTHOR)) //
					.date(new Date(item.getLong(DATE))) //
					.comment(item.getString(COMMENT));

			quizComments.add(comment);
		}

		return quizComments;
	}
}
