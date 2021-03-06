package com.ken.aws.quiz.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ken.aws.quiz.dao.QuizCommentDao;
import com.ken.aws.quiz.dao.QuizDao;
import com.ken.aws.quiz.model.QuizComment;
import com.ken.aws.quiz.model.QuizEntity;
import com.ken.aws.quiz.service.StaticFileService;

@RestController()
public class QuizController {

	private static final String LEGACY_PRFIX = "aws-quiz-";

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuizCommentDao commentDao;

	@Autowired
	private StaticFileService staticFileService;

	@Value("${server.index.page:classpath:/static/index.html}")
	private Resource indexPage;

	private static final Log LOG = LogFactory.getLog(QuizController.class);
	private static final String HTML_HEADER = "<!DOCTYPE html><html><head><title>";
	private static final String HTML_HEADER2 = "</title><link rel=\"shortcut icon\" href=\"https://s3-ap-southeast-1.amazonaws.com/kennie-quiz/quiz-icon.ico\"></head><body>";

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		String htmlString = staticFileService.readFileToEnd(indexPage);

		if (htmlString == null) {
			htmlString = "<!DOCTYPE html><html><h1>Hi, Welcome to my Server!</h1><font color='red'>Page not found</font><html>";
		}

		return htmlString;
	}

	@RequestMapping("/{category}")
	public String list(@PathVariable("category") final String pCategory) {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";
		final Long maxNum = quizDao.getMaxNum(category);
		String htmlString = HTML_HEADER + "Quiz - " + prefix + category + HTML_HEADER2
				+ "<a href='/'>Home</a><p>Quiz List: <P>";

		if (maxNum > 0) {
			htmlString += "<a href='/" + prefix + category + "/1'>Quiz 1</a>";
			for (int i = 2; i <= maxNum; i++) {
				if (i % 10 == 1) {
					htmlString += "<p>";
				} else {
					htmlString += "&nbsp;|&nbsp;";
				}
				htmlString += "<a href='/" + prefix + category + "/" + i + "'>Quiz " + i + "</a>";
			}
		} else {
			htmlString += "<p>No Question in the list";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/{category}/add", method = RequestMethod.GET)
	public String addQuizPage(@PathVariable("category") final String pCategory, //
			final HttpServletRequest httpServletRequest) {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";

		final String message = httpServletRequest.getParameter("message");
		final String num = httpServletRequest.getParameter("num");

		String htmlString = HTML_HEADER + "Create Quiz - " + prefix + category + HTML_HEADER2;

		if ("success".equals(message) && num != null) {
			htmlString += "<font color='green'><b>Success added record Quiz " + num + "</b></font><p>";
		} else if (message != null && !"".equals(message.trim())) {
			htmlString += "<font color='red'><b>Failed - " + message + "</b></font><p>";
		}

		htmlString += "<a href='/" + prefix + category + "'>Back to List</a><p>Adding Quiz - " + prefix + category
				+ ": <p><form id='quizAdd' action='/" + prefix + category + "/add' method='POST'>" //
				+ "<table><tr><td>Description: </td><td><textarea name='Desc' form='quizAdd' rows=\"10\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Question: </td><td><textarea name='Title' form='quizAdd' rows=\"5\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Choices: </td><td><textarea name='Choices' form='quizAdd' rows=\"15\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Answer: </td><td><input type='text' name='Answer'></td></tr></table>" //
				+ "<input type='submit'/>" //
				+ "</form></body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/{category}/add", method = RequestMethod.POST)
	public void addQuiz(@PathVariable("category") final String pCategory, //
			@RequestParam("Title") final String title, //
			@RequestParam("Desc") final String desc, //
			@RequestParam("Choices") final String choices, //
			@RequestParam("Answer") final String answer, //
			final HttpServletResponse response) throws IOException {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";

		final Long nextNum = quizDao.getMyCheck(category) + 1;
		final QuizEntity quiz = new QuizEntity() //
				.category(category) //
				.num(nextNum) //
				.answer(answer) //
				.choices(choices) //
				.title(title) //
				.desc(desc);
		try {
			quizDao.addQuiz(quiz);
		} catch (final RuntimeException e) {
			LOG.warn(e.getMessage());
			response.sendRedirect("/" + prefix + category + "/add?message="
					+ java.net.URLEncoder.encode(e.getMessage(), "ISO-8859-1"));
		}

		response.sendRedirect("/" + prefix + category + "/add?message=success&num=" + nextNum);
	}

	@RequestMapping(path = "/{category}/{id}/comment", method = RequestMethod.POST)
	public void addQuizComment(@PathVariable("category") final String pCategory, //
			@PathVariable("id") final Long id, //
			@RequestParam("author") final String author, //
			@RequestParam("comment") final String comment, //
			final HttpServletResponse response) throws IOException {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";

		final QuizComment quizComment = new QuizComment().quizId(category + '-' + id).author(author).comment(comment);

		commentDao.addComment(quizComment);

		response.sendRedirect("/" + prefix + category + "/" + id);
	}

	@RequestMapping("/{category}/{id}")
	public String getQuiz(@PathVariable("category") final String pCategory, //
			@PathVariable("id") final Long id) {
		final String category = pCategory.startsWith(LEGACY_PRFIX) ? pCategory.replaceAll(LEGACY_PRFIX, "") : pCategory;
		final String prefix = pCategory != category ? LEGACY_PRFIX : "";

		final Long maxNum = quizDao.getMaxNum(category);
		final QuizEntity quiz = quizDao.readQuiz(category, id);
		String htmlString = HTML_HEADER + prefix + category + " Quiz " + id + HTML_HEADER2 + "<a href='/" + prefix
				+ category + "'>Quiz List</a><p>";
		if (id > 1) {
			htmlString += "<a href='/" + prefix + category + "/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/" + prefix + category + "/" + (id + 1) + "'>Next Quiz</a>";
		}
		htmlString += "<p>Quiz " + id + "  <p>";

		if (quiz != null) {
			htmlString += "<table border='1'>";

			final String desc = quiz.getDesc();
			if (desc != null && !"".equals(desc.trim())) {
				htmlString += "<tr><td><b>Description</b></td><td><h1><pre>" + desc + "</pre></h1></td></tr>";
			}

			final List<QuizComment> comments = commentDao.readCommenByQuiz(category + '-' + id);
			htmlString += "<tr><td><b>Question</b></td><td><h1><pre>" + quiz.getTitle() + "</pre></h1></td></tr>" //
					+ "<tr><td><b>Choices<b></td><td><h3><pre>" + quiz.getChoices() + "</pre></h3></td></tr>" //
					+ "<tr><td><b>Answer</b></td><td><h3><font id='answerBox' color='red'>"
					+ "<div id='divAnswer' style='display: none'>" + quiz.getAnswer()
					+ "</div></h3></font><button id='btnAnswer' onClick='getElementById(\"divAnswer\").style.display=\"block\"; "
					+ "getElementById(\"btnAnswer\").style.display=\"none\"; ' >Show Answer</button></td></tr>"
					+ "<tr><td><b>" + (comments.isEmpty() ? "No Comment" : "Comment (" + comments.size() + ")")
					+ "</td><td>";

			final String addCommentHtml = "<button id='addComment' onclick='getElementById(\"addComment\").style.display=\"none\";"
					+ "getElementById(\"commentForm\").style.display=\"block\" ' >Add Comment</button><div id='commentForm' style='display: none'>"
					+ "<form id='commentAdd' action='/" + prefix + category + "/" + id + "/comment' method='POST'>"
					+ "<table><tr><td>Author:</td><td><input name='author' /></td></tr>"
					+ "<tr><td>Comment:</td><td><textarea name='comment' form='commentAdd'  rows='5' cols='200'></textarea></table>"
					+ "<input type='submit'/></form></div>";

			if (comments.isEmpty()) {
				htmlString += addCommentHtml;
			} else {
				QuizComment comment = comments.get(0);
				htmlString += "<button id='btnShowComment' onClick='getElementById(\"commentBox\").style.display = \"block\"; "
						+ "getElementById(\"btnShowComment\").style.display=\"none\" ' >Show Comment</button>"
						+ "<div id='commentBox' style='display:none'>" + addCommentHtml + comment.getHtml();

				for (int i = 1, size = comments.size(); i < size; i++) {
					comment = comments.get(i);
					htmlString += "<hr />" + comment.getHtml();
				}

				htmlString += "</div>";
			}

			htmlString += "</td></tr></table><p>";
		} else {
			htmlString += "Question Not found<p>";
		}

		if (id > 1) {
			htmlString += "<a href='/" + prefix + category + "/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/" + prefix + category + "/" + (id + 1) + "'>Next Quiz</a>";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

}
