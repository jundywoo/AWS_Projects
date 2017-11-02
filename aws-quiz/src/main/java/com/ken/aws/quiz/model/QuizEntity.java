package com.ken.aws.quiz.model;

public class QuizEntity {

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

	public QuizEntity category(String category) {
		this.category = category;
		return this;
	}

	public QuizEntity num(Long num) {
		this.num = num;
		return this;
	}

	public QuizEntity title(String title) {
		this.title = title;
		return this;
	}

	public QuizEntity choices(String choices) {
		this.choices = choices;
		return this;
	}

	public QuizEntity desc(String desc) {
		this.desc = desc;
		return this;
	}

	public QuizEntity answer(String answer) {
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

}
