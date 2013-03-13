package me.kucoo.graph.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import me.kucoo.graph.rule.IRule;
import me.kucoo.graph.rule.RuleFactory;

public class QuestionGeneratorServlet extends HttpServlet {
	private static IRule rule = RuleFactory.getRule(QuestionType.FILM_COMBO);

	static {
		ApplicationContext.getInstace();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws IOException, ServletException {
		String keyword = (String) req.getParameter("keyword");
		System.out.println("Keyword = : " + keyword);
		QuestionEntity item = rule.generate(keyword);

		if (item != null) {
			res.setContentType("text/html");

			PrintWriter out = res.getWriter();
			out.println("");
			out.println("Question : " + item.question);
			out.println("<br>");
			out.println("<br><span style=\"padding-left:20px\">1. " + item.possibleAnwers[0] + "</span>");
			out.println("<br><span style=\"padding-left:20px\">2. " + item.possibleAnwers[1] + "</span>");
			out.println("<br><span style=\"padding-left:20px\">3. " + item.possibleAnwers[2] + "</span>");
			out.println("<br><span style=\"padding-left:20px\">4. " + item.possibleAnwers[3] + "</span>");
			out.println("<br><br><br><br><br><br><br><br><br><br><br><br>");
			out.println("This is the answer. Try not to look at it next time :) :");
			out.println("<br><br><span style=\"padding-left:20px\">Answer : " + item.answer + "</span>");

			out.close();
		} else {
			res.setContentType("text/html");

			PrintWriter out = res.getWriter();
			out.println("");
			out.println("Bad keyword - I can't generate a question.");
			out.close();
		}

	}
}