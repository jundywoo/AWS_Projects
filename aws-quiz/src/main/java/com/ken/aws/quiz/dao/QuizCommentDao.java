package com.ken.aws.quiz.dao;

import static com.ken.aws.quiz.model.QuizComment.AUTHOR;
import static com.ken.aws.quiz.model.QuizComment.COMMENT;
import static com.ken.aws.quiz.model.QuizComment.DATE;
import static com.ken.aws.quiz.model.QuizComment.QUIZ_ID;
import static com.ken.aws.quiz.model.QuizComment.TABLE_NAME_QUIZ_COMMENT;

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
import com.ken.aws.quiz.model.QuizComment;

@Service
public class QuizCommentDao extends DynamoDBDaoSupport {

	public List<QuizComment> readCommenByQuiz(String quizId) {
		List<QuizComment> quizComments = new ArrayList<>();

		Table table = getTable(TABLE_NAME_QUIZ_COMMENT);
		QuerySpec spec = new QuerySpec() //
				.withKeyConditionExpression(QUIZ_ID + " = :value_num") //
				.withValueMap(new ValueMap().with(":value_num", quizId));

		ItemCollection<QueryOutcome> items = table.query(spec);

		for (Item item : items) {
			QuizComment comment = new QuizComment() //
					.quizId(item.getString(QUIZ_ID)) //
					.author(item.getString(AUTHOR)) //
					.date(new Date(item.getLong(DATE))) //
					.comment(item.getString(COMMENT));

			quizComments.add(comment);
		}

		return quizComments;
	}

	public void addComment(QuizComment quizComment) {
		final String comment = quizComment.getComment();
		if (comment == null || "".equals(comment.trim())) {
			return;
		}
		final Table table = getTable(TABLE_NAME_QUIZ_COMMENT);
		final Item item = new Item() //
				.withPrimaryKey(QUIZ_ID, quizComment.getQuizId()) //
				.with(COMMENT, comment) //
				.with(AUTHOR, quizComment.getAuthor()) //
				.with(DATE, System.currentTimeMillis());

		table.putItem(item);
	}
}
