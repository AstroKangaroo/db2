package it.polimi.db2.gunalanodabasi.controllers;

import java.io.*;
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

import it.polimi.db2.gunalanodabasi.entities.Forbiddenwords;
import it.polimi.db2.gunalanodabasi.entities.Question;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;
import it.polimi.db2.gunalanodabasi.entities.User;
import it.polimi.db2.gunalanodabasi.services.AnswerService;
import it.polimi.db2.gunalanodabasi.services.ForbiddenwordsService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.SessionService;
import it.polimi.db2.gunalanodabasi.services.StatisticalanswerService;
import it.polimi.db2.gunalanodabasi.services.SubmitstatusService;
import it.polimi.db2.gunalanodabasi.services.UserService;


@WebServlet("/SubmitQuestionnaire")
public class SubmitQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qrService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/AnswerService")
	private AnswerService aService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/StatisticalanswerService")
	private StatisticalanswerService saService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/SubmitstatusService")
	private SubmitstatusService ssService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/ForbiddenwordsService")
	private ForbiddenwordsService fService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/UserService")
	private UserService uService;
	

	public SubmitQuestionnaire() {
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
		SessionService SessionService = (SessionService) request.getSession().getAttribute("sessionService");
		User u = SessionService.getUser();
		
		List<String> answers = SessionService.getAnswers();

		String age = request.getParameterValues("age")[0];
		if(age.isBlank())
			age = null;
		String sex = request.getParameterValues("sex")[0];
		if(sex.isBlank())
			sex = null;
		String expertise = request.getParameterValues("expertise")[0];
		if(expertise.isBlank())
			expertise = null;
	
		
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);		
		Questionnaire qr = null;
		qr = qrService.findByDate(date);
		
		List<Question> questions = null;
		questions = qr.getQuestions();
		
		List<Forbiddenwords> forbiddenwords = fService.findAllForbiddenwords();
		
		boolean shouldUserBeBanned = false;
		for(int i = 0; i < forbiddenwords.size(); i++)
			for(int j = 0; j < answers.size(); j++) {
				if(answers.get(j).contains(forbiddenwords.get(i).getText()))
					shouldUserBeBanned = true;
			}
		if(shouldUserBeBanned) {
			uService.banUser(u.getId());
			SessionService.getUser().setIsbanned(1);
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
		else {
			ssService.createSubmitstatus(u.getId(), qr.getId(), "Submitted");

			for(int i = 0; i < answers.size(); i++) {
				aService.createAnswer(u.getId(), questions.get(i).getId(), qr.getId(), answers.get(i));
			}
			if(age != null | sex != null | expertise != null)
				saService.createStatisticalanswer(u.getId(), qr.getId(), age, sex, expertise);	
			
			String path = "/WEB-INF/Thankyou.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(path, ctx, response.getWriter());
		}
		
		
		
	}
}