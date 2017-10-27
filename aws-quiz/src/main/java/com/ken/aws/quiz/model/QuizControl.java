package com.ken.aws.quiz.model;

public class QuizControl {

	public static final String CONTROL_FIELD = "control_field";
	public static final String VALUE = "value";

	private String controlField;
	private Long value;

	public QuizControl controlField(String controlField) {
		this.controlField = controlField;
		return this;
	}

	public QuizControl value(long value) {
		this.value = value;
		return this;
	}

	public String getControlField() {
		return controlField;
	}

	public Long getValue() {
		return value;
	}

}
