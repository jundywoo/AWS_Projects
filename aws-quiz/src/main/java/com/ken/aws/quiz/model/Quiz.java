package com.ken.aws.quiz.model;

public class Quiz {

	public static final String QUIZ_ID = "quiz_id";
	public static final String TITLE = "title";
	public static final String CHOICES = "choices";
	public static final String DESC = "desc";
	public static final String ANSWER = "answer";

	private String quizId;
	private String title;
	private String desc;
	private String choices;
	private String answer;

	public Quiz quizId(String quizId) {
		this.quizId = quizId;
		return this;
	}

	public Quiz title(String title) {
		this.title = title;
		return this;
	}

	public Quiz choices(String choices) {
		this.choices = choices;
		return this;
	}

	public Quiz desc(String desc) {
		this.desc = desc;
		return this;
	}

	public Quiz answer(String answer) {
		this.answer = answer;
		return this;
	}

	public String getQuizId() {
		return quizId;
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

}
