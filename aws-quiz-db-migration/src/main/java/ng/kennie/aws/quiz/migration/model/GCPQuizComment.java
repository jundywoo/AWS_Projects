package ng.kennie.aws.quiz.migration.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GCPQuizComment implements Comparable<GCPQuizComment> {

	public static final String KIND_QUIZ_COMMENT = "quiz_comment";

	// Primary Key
	public static final String QUIZ_ID = "quiz_id";

	// Data field
	public static final String AUTHOR = "author";
	public static final String DATE = "date";
	public static final String COMMENT = "comment";

	private String quizId;
	private String author;
	private Date date;
	private String comment;

	public String getQuizId() {
		return quizId;
	}

	public String getAuthor() {
		return author;
	}

	public Date getDate() {
		return date;
	}

	public String getComment() {
		return comment;
	}

	public GCPQuizComment quizId(String quizId) {
		this.quizId = quizId;
		return this;
	}

	public GCPQuizComment author(String author) {
		this.author = author;
		return this;
	}

	public GCPQuizComment comment(String comment) {
		this.comment = comment;
		return this;
	}

	public GCPQuizComment date(Date date) {
		this.date = date;
		return this;
	}

	public String getHtml() {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
		return "<p><i><b>" + this.getAuthor() + "</b> comments at <b>" + format.format(this.getDate())
				+ "</b></i><p><div style='width: 60%'><pre style='white-space:pre-line;'>" + this.comment
				+ "</pre></div>";
	}

	@Override
	public int compareTo(GCPQuizComment o) {
		return this.date.compareTo(o.date);
	}

}
