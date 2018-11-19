package ng.kennie.aws.quiz.migration.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MigrationController {

	private static final String LEGACY_PRFIX = "aws-quiz-";
	private static final Pattern PURE_DIGIT = Pattern.compile("\\d+");
	private static final Pattern FROM_TO_DIGIT = Pattern.compile("(\\d+)-(\\d+)");

	private static final Log LOG = LogFactory.getLog(MigrationController.class);

	// @Autowired
	// private AWSQuizDao awsQuizDao;

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
				final long fromNum = Long.parseLong(matcher.group(0));
				final long toNum = Long.parseLong(matcher.group(1));

				for (long i = fromNum; i <= toNum; i++) {
					doMigrateQuiz(prefix, category, i);
				}
			}
		}

		return "Migrate Quiz: " + category + " on " + num;
	}

	private void doMigrateQuiz(final String prefix, final String category, final long num) {
		LOG.info(String.format("Migrating: %s-%s, %d", prefix, category, num));
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
				final long fromNum = Long.parseLong(matcher.group(0));
				final long toNum = Long.parseLong(matcher.group(1));

				for (long i = fromNum; i <= toNum; i++) {
					doMigrateComment(prefix, category, i);
				}
			}
		}

		return "Migrate QuizComment: " + category + " on " + num;
	}

	private void doMigrateComment(final String prefix, final String category, final long num) {
		LOG.info(String.format("Migrating: %s-%s, %d", prefix, category, num));
	}

}
