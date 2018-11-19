package ng.kennie.aws.quiz.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import ng.kennie.aws.quiz.migration.config.QuizConfiguration;

@ComponentScan("ng.kennie.aws.quiz.migration")
@EnableAutoConfiguration
@Import(QuizConfiguration.class)
public class Application {

	public static void main(final String[] args) throws InstantiationException, IllegalAccessException {
		SpringApplication.run(Application.class, args);
	}

}
