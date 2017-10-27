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
import com.ken.aws.quiz.model.Quiz;
import com.ken.aws.quiz.model.QuizComment;
import com.ken.aws.quiz.service.StaticFileService;

@RestController()
public class QuizController {

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
		String htmlString = null;
		try {
			htmlString = staticFileService.readFileToEnd(indexPage.getFile());
		} catch (IOException e) {
			LOG.warn(e.getMessage());
		}

		if (htmlString == null) {
			htmlString = "<!DOCTYPE html><html><h1>Hi, Welcome to my Server!</h1><font color='red'>Page not found</font><html>";
		}

		return htmlString;
	}

	@RequestMapping("/aws-{category}-quiz")
	public String list(@PathVariable("category") String category) {
		Long maxNum = quizDao.maxNum(category);
		String htmlString = HTML_HEADER + "AWS Quiz - " + category.toUpperCase() + HTML_HEADER2
				+ "<a href='/'>Home</a><p>List: ";

		if (maxNum > 0) {
			htmlString += "<a href='/aws-" + category + "-quiz/1'>Quiz 1</a>";
			for (int i = 2; i <= maxNum; i++) {
				htmlString += "&nbsp;|&nbsp;<a href='/aws-" + category + "-quiz/" + i + "'>Quiz " + i + "</a>";
			}
		} else {
			htmlString += "<p>No Question in the list";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/aws-{category}-quiz/add", method = RequestMethod.GET)
	public String addQuizPage(@PathVariable("category") String category, //
			HttpServletRequest httpServletRequest) {
		String message = httpServletRequest.getParameter("message");
		String num = httpServletRequest.getParameter("num");

		String htmlString = HTML_HEADER + "Create AWS Quiz - " + category.toUpperCase() + HTML_HEADER2;

		if ("success".equals(message) && num != null) {
			htmlString += "<font color='green'><b>Success added record Quiz " + num + "</b></font><p>";
		}

		htmlString += "<a href='/aws-" + category + "-quiz'>Back to List</a><p>Adding Quiz - " + category.toUpperCase()
				+ ": <p><form id='quizAdd' action='/aws-" + category + "-quiz/add' method='POST'>" //
				+ "<table><tr><td>Description: </td><td><textarea name='Desc' form='quizAdd' rows=\"10\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Question: </td><td><textarea name='Title' form='quizAdd' rows=\"5\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Choices: </td><td><textarea name='Choices' form='quizAdd' rows=\"15\" cols=\"200\"></textarea></td></tr>" //
				+ "<tr><td>Answer: </td><td><input type='text' name='Answer'></td></tr></table>" //
				+ "<input type='submit'/>" //
				+ "</form></body><html>";
		return htmlString;
	}

	@RequestMapping(path = "/aws-{category}-quiz/add", method = RequestMethod.POST)
	public void addQuiz(@PathVariable("category") String category, //
			@RequestParam("Title") String title, //
			@RequestParam("Desc") String desc, //
			@RequestParam("Choices") String choices, //
			@RequestParam("Answer") String answer, //
			HttpServletResponse response) throws IOException {
		Long maxNum = quizDao.maxNum(category);
		Quiz quiz = new Quiz().num(maxNum + 1).answer(answer).choices(choices).title(title).desc(desc);
		quizDao.addQuiz(category, quiz);

		response.sendRedirect("/aws-" + category + "-quiz/add?message=success&num=" + quiz.getNum());
	}

	@RequestMapping(path = "/aws-{category}-quiz/{id}/comment", method = RequestMethod.POST)
	public void addQuizComment(@PathVariable("category") String category, //
			@PathVariable("id") Long id, //
			@RequestParam("author") String author, //
			@RequestParam("comment") String comment, //
			HttpServletResponse response) throws IOException {
		QuizComment quizComment = new QuizComment().num(id).author(author).comment(comment);

		commentDao.addComment(category, quizComment);

		response.sendRedirect("/aws-" + category + "-quiz/" + id);
	}

	@RequestMapping("/aws-{category}-quiz/{id}")
	public String getQuiz(@PathVariable("category") String category, //
			@PathVariable("id") Long id) {
		Long maxNum = quizDao.maxNum(category);
		Quiz quiz = quizDao.readQuiz(category, id);
		String htmlString = HTML_HEADER + category.toUpperCase() + " Quiz " + id + HTML_HEADER2 + "<a href='/aws-"
				+ category + "-quiz'>Quiz List</a><p>Quiz " + id + "  <p>";

		if (quiz != null) {
			htmlString += "<table border='1'>";

			String desc = quiz.getDesc();
			if (desc != null && !"".equals(desc.trim())) {
				htmlString += "<tr><td><b>Description</b></td><td><h1><pre>" + desc + "</pre></h1></td></tr>";
			}

			List<QuizComment> comments = commentDao.readCommenByQuiz(category, id);
			htmlString += "<tr><td><b>Question</b></td><td><h1><pre>" + quiz.getTitle() + "</pre></h1></td></tr>" //
					+ "<tr><td><b>Choices<b></td><td><h3><pre>" + quiz.getChoices() + "</pre></h3></td></tr>" //
					+ "<tr><td><b>Answer</b></td><td><h3><font id='answerBox' color='red'>"
					+ "<div id='divAnswer' style='display: none'>" + quiz.getAnswer()
					+ "</div></h3></font><button id='btnAnswer' onClick='getElementById(\"divAnswer\").style.display=\"block\"; "
					+ "getElementById(\"btnAnswer\").style.display=\"none\"; ' >Show Answer</button></td></tr>"
					+ "<tr><td><b>" + (comments.isEmpty() ? "No Comment" : "Comment (" + comments.size() + ")")
					+ "</td><td>";

			String addCommentHtml = "<button id='addComment' onclick='getElementById(\"addComment\").style.display=\"none\";"
					+ "getElementById(\"commentForm\").style.display=\"block\" ' >Add Comment</button><div id='commentForm' style='display: none'>"
					+ "<form id='commentAdd' action='/aws-" + category + "-quiz/" + id + "/comment' method='POST'>"
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
			htmlString += "<a href='/aws-" + category + "-quiz/" + (id - 1) + "'>Previous Quiz</a>";
		}
		if (id < maxNum) {
			htmlString += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='/aws-" + category + "-quiz/" + (id + 1) + "'>Next Quiz</a>";
		}

		htmlString += "</body><html>";
		return htmlString;
	}

}
