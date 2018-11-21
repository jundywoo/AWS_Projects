package ng.kennie.aws.quiz.migration.model;

public class GCPQuizEntity {

	public static final String TABLE_NAME_QUIZ_ENTITY = "quiz_entity";

	// Primary key
	public static final String CATEGORY = "category";
	public static final String NUM = "num";

	// Data fields
	public static final String TITLE = "title";
	public static final String CHOICES = "choices";
	public static final String DESC = "desc";
	public static final String ANSWER = "answer";

	private String category;
	private Long num;
	private String title;
	private String desc;
	private String choices;
	private String answer;

	public GCPQuizEntity category(final String category) {
		this.category = category;
		return this;
	}

	public GCPQuizEntity num(final Long num) {
		this.num = num;
		return this;
	}

	public GCPQuizEntity title(final String title) {
		this.title = title;
		return this;
	}

	public GCPQuizEntity choices(final String choices) {
		this.choices = choices;
		return this;
	}

	public GCPQuizEntity desc(final String desc) {
		this.desc = desc;
		return this;
	}

	public GCPQuizEntity answer(final String answer) {
		this.answer = answer;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public String getChoices() {
		return choices;
	}

	public String getAnswer() {
		return answer;
	}

	public String getDesc() {
		return desc;
	}

	public String getCategory() {
		return category;
	}

	public Long getNum() {
		return num;
	}

	public String getKey() {
		return category + "_" + num;
	}

}
