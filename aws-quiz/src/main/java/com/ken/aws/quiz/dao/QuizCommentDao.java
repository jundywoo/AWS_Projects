package com.ken.aws.quiz.dao;

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
import com.ken.aws.quiz.model.Quiz;
import com.ken.aws.quiz.model.QuizComment;

@Service
public class QuizCommentDao extends DynamoDBDaoSupport {

	public static final String TABLE_NAME_QUIZ_COMMENT = "quiz_comment";

	public List<QuizComment> readCommenByQuiz(String quizId) {
		List<QuizComment> quizComments = new ArrayList<>();

		Table table = getTable(TABLE_NAME_QUIZ_COMMENT);
		QuerySpec spec = new QuerySpec() //
				.withKeyConditionExpression(Quiz.QUIZ_ID + " = :value_num") //
				.withValueMap(new ValueMap().with(":value_num", quizId));

		ItemCollection<QueryOutcome> items = table.query(spec);

		for (Item item : items) {
			QuizComment comment = new QuizComment() //
					.quizId(item.getString(QuizComment.QUIZ_ID)) //
					.author(item.getString(QuizComment.AUTHOR)) //
					.date(new Date(item.getLong(QuizComment.DATE))) //
					.comment(item.getString(QuizComment.COMMENT));

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
				.with(QuizComment.QUIZ_ID, quizComment.getQuizId()) //
				.with(QuizComment.COMMENT, comment) //
				.with(QuizComment.AUTHOR, quizComment.getAuthor()) //
				.with(QuizComment.DATE, System.currentTimeMillis());

		table.putItem(item);
	}
}
