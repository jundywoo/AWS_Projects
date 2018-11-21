package ng.kennie.aws.quiz.migration.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ng.kennie.aws.quiz.migration.dao.AWSQuizDao;
import ng.kennie.aws.quiz.migration.dao.GCPQuizDao;
import ng.kennie.aws.quiz.migration.model.AWSQuizComment;
import ng.kennie.aws.quiz.migration.model.AWSQuizEntity;
import ng.kennie.aws.quiz.migration.model.GCPQuizComment;
import ng.kennie.aws.quiz.migration.model.GCPQuizEntity;

@RestController()
public class MigrationController {

	private static final String LEGACY_PRFIX = "aws-quiz-";
	private static final Pattern PURE_DIGIT = Pattern.compile("^\\d+$");
	private static final Pattern FROM_TO_DIGIT = Pattern.compile("^(\\d+)-(\\d+)$");

	private static final Log LOG = LogFactory.getLog(MigrationController.class);

	@Autowired
	private AWSQuizDao awsQuizDao;

	@Autowired
	private GCPQuizDao gcpQuizDao;

	@RequestMapping("migrateQuiz/{category}/{num}")
	public String migrateQuiz(@PathVariable("category") final String pCategory, //
			@PathVariable("num") final String num) {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";

		if (PURE_DIGIT.matcher(num).find()) {
			doMigrateQuiz(prefix, category, Long.parseLong(num));
		} else {
			final Matcher matcher = FROM_TO_DIGIT.matcher(num);
			if (matcher.find()) {
				final long fromNum = Long.parseLong(matcher.group(1));
				final long toNum = Long.parseLong(matcher.group(2));

				for (long i = fromNum; i <= toNum; i++) {
					doMigrateQuiz(prefix, category, i);
				}
			} else {
				return "Not match the pattern, " + prefix + category + " on " + num;
			}
		}

		return "Migrate Quiz: " + prefix + category + " on " + num;
	}

	private void doMigrateQuiz(final String prefix, final String category, final long num) {
		final AWSQuizEntity awsQuiz = awsQuizDao.readQuiz(category, num);

		final GCPQuizEntity gcpQuiz = new GCPQuizEntity() //
				.category(prefix + category) //
				.num(num) //
				.title(awsQuiz.getTitle()) //
				.desc(awsQuiz.getDesc()) //
				.choices(awsQuiz.getChoices()) //
				.answer(awsQuiz.getAnswer());
		gcpQuizDao.addQuiz(gcpQuiz);

		LOG.info(String.format("Migrated: %s%s, %d", prefix, category, num));
	}

	@RequestMapping("migrateComment/{category}/{num}")
	public String migrateComment(@PathVariable("category") final String pCategory, //
			@PathVariable("num") final String num) {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";

		if (PURE_DIGIT.matcher(num).find()) {
			doMigrateComment(prefix, category, Long.parseLong(num));
		} else {
			final Matcher matcher = FROM_TO_DIGIT.matcher(num);
			if (matcher.find()) {
				final long fromNum = Long.parseLong(matcher.group(1));
				final long toNum = Long.parseLong(matcher.group(2));

				for (long i = fromNum; i <= toNum; i++) {
					doMigrateComment(prefix, category, i);
				}
			} else {
				return "Not match the pattern, " + prefix + category + " on " + num;
			}
		}

		return "Migrate QuizComment: " + prefix + category + " on " + num;
	}

	private void doMigrateComment(final String prefix, final String category, final long num) {
		final List<AWSQuizComment> awsComments = awsQuizDao.readCommenByQuiz(category + "-" + num);
		for (final AWSQuizComment awsComment : awsComments) {
			final GCPQuizComment gcpComment = new GCPQuizComment() //
					.author(awsComment.getAuthor())//
					.date(awsComment.getDate()) //
					.quizId(prefix + awsComment.getQuizId()) //
					.comment(awsComment.getComment());

			gcpQuizDao.addComment(gcpComment);
		}
		LOG.info(String.format("Migrating: %s%s, %d", prefix, category, num));
	}

}
