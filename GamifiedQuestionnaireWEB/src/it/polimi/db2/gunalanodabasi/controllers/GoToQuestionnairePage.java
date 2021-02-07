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
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gunalanodabasi.entities.*;
import it.polimi.db2.gunalanodabasi.services.QuestionService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.SessionService;
import it.polimi.db2.gunalanodabasi.services.SubmitstatusService;


@WebServlet("/GoToQuestionnairePage")
public class GoToQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionService")
	private QuestionService qService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qrService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/SubmitstatusService")
	private SubmitstatusService ssService;


	public GoToQuestionnairePage() {
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
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionService SessionService = (SessionService) request.getSession().getAttribute("sessionService");
		
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		Questionnaire qr = null;
		qr = qrService.findByDate(date);
		
		if(SessionService.getUser().getIsbanned() == 1) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				SessionService ss = (SessionService) session.getAttribute("sessionService");
				if (ss != null) ss.remove();
				session.invalidate();
			}
			String path = "/WEB-INF/Banned.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(path, ctx, response.getWriter());
		}
		else if(ssService.findSubmitstatus(SessionService.getUser().getId(), qr.getId()) != null){
			String path = "/WEB-INF/QuestionnaireFilled.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(path, ctx, response.getWriter());
		}
		else {
			List<String> answers = SessionService.getAnswers();
			if(answers.size() == 0)
				answers = null;

			
			Product p = null;
			List<Question> questions = null;
			
			
			if(qr != null) {
				questions = qr.getQuestions();
				p = qr.getProduct();
			}
			if(answers == null) {
				answers = new ArrayList<String>();
				for(int i = 0; i < questions.size(); i++) {
					answers.add("");
				}
			}
			
			String path = "/WEB-INF/Questionnaire.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("questions", questions);
			if (p != null) {
				ctx.setVariable("product", p);
			}
			ctx.setVariable("answers", answers);
			templateEngine.process(path, ctx, response.getWriter());
		}
		
	}
}