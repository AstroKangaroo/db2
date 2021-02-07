package it.polimi.db2.gunalanodabasi.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gunalanodabasi.entities.*;
import it.polimi.db2.gunalanodabasi.services.QuestionService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.SessionService;


@WebServlet("/GoToQuestionnaireNextPage")
public class GoToQuestionnaireNextPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionService")
	private QuestionService qService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qrService;


	public GoToQuestionnaireNextPage() {
		super();
	}
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
				response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> questions = new ArrayList<String>();
		questions.add("Age");
		questions.add("Sex");
		questions.add("Expertise Level");
				
		SessionService SessionService = (SessionService) request.getSession().getAttribute("sessionService");
		String[] answers = request.getParameterValues("answer");
		for(int i = 0; i < answers.length; i++) {
			if(SessionService.getAnswers().size() > i) {
				SessionService.changeAnswer(i, answers[i]);
			}
			else {
				SessionService.addAnswer(answers[i]);
			}
		}

		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		
		Questionnaire qr = null;
		Product p = null;
		
		qr = qrService.findByDate(date);
		if(qr != null) {
			p = qr.getProduct();
		}
		
		String path = "/WEB-INF/QuestionnaireNext.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questions", questions);
		if (p != null) {
			ctx.setVariable("product", p);
		}
		templateEngine.process(path, ctx, response.getWriter());
	}
}