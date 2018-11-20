package ng.kennie.aws.quiz.migration.dao;

import static ng.kennie.aws.quiz.migration.model.GCPQuizComment.AUTHOR;
import static ng.kennie.aws.quiz.migration.model.GCPQuizComment.COMMENT;
import static ng.kennie.aws.quiz.migration.model.GCPQuizComment.DATE;
import static ng.kennie.aws.quiz.migration.model.GCPQuizComment.QUIZ_ID;
import static ng.kennie.aws.quiz.migration.model.GCPQuizComment.KIND_QUIZ_COMMENT;
import static ng.kennie.aws.quiz.migration.model.GCPQuizEntity.ANSWER;
import static ng.kennie.aws.quiz.migration.model.GCPQuizEntity.CHOICES;
import static ng.kennie.aws.quiz.migration.model.GCPQuizEntity.DESC;
import static ng.kennie.aws.quiz.migration.model.GCPQuizEntity.TITLE;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.ken.gcp.quiz.utils.MyValueUtils;

import ng.kennie.aws.quiz.migration.model.GCPQuizComment;
import ng.kennie.aws.quiz.migration.model.GCPQuizEntity;

@Service
public class GCPQuizDao implements InitializingBean {

	public static final String KIND_QUIZ_CONTROL = "quiz_control";
	public static final String KIND_QUIZ_ENTITY = "quiz_entity";

	private Datastore datastore;
	private KeyFactory entityKeyFactory;
	private KeyFactory commentKeyFactory;

	@Override
	public void afterPropertiesSet() throws Exception {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
		entityKeyFactory = datastore.newKeyFactory().setKind(KIND_QUIZ_ENTITY);
		commentKeyFactory = datastore.newKeyFactory().setKind(KIND_QUIZ_COMMENT);
	}

	public Entity addQuiz(final GCPQuizEntity quiz) {
		final IncompleteKey key = entityKeyFactory.newKey(quiz.getKey()); // Key will be assigned once written
		final FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(TITLE, MyValueUtils.noIndexString(quiz.getTitle())) //
				.set(DESC, MyValueUtils.noIndexString(quiz.getDesc())) //
				.set(CHOICES, MyValueUtils.noIndexString(quiz.getChoices())) //
				.set(ANSWER, MyValueUtils.noIndexString(quiz.getAnswer())).build();
		final Entity addedEntity = datastore.add(entity); // Save the Entity

		return addedEntity;
	}

	public Entity addComment(final GCPQuizComment quizComment) {
		final IncompleteKey key = commentKeyFactory.newKey(); // Key will be assigned once written
		final FullEntity<IncompleteKey> entity = Entity.newBuilder(key) // Create the Entity
				.set(QUIZ_ID, quizComment.getQuizId()) //
				.set(DATE, new Date().getTime()) //
				.set(AUTHOR, MyValueUtils.noIndexString(quizComment.getAuthor())) //
				.set(COMMENT, MyValueUtils.noIndexString(quizComment.getComment())).build();
		final Entity addedEntity = datastore.add(entity); // Save the Entity

		return addedEntity;
	}
}
