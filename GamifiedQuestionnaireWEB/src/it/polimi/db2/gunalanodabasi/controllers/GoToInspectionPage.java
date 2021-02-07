package it.polimi.db2.gunalanodabasi.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;

import it.polimi.db2.gunalanodabasi.entities.*;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.UserService;

import java.util.ArrayList;
import java.util.List;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet("/GoToInspectionPage")
public class GoToInspectionPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/UserService")
	private UserService uService;

	public GoToInspectionPage() {
		super();
		// TODO Auto-generated constructor stub
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
		
		List<Questionnaire> questionnaires = null;
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		questionnaires = qService.findAllBeforeDate(date);
		String path = "/WEB-INF/Inspection.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questionnaires", questionnaires);
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Questionnaire> questionnaires = null;
		List<Answer> answers = new ArrayList<Answer>();
		Questionnaire selectedQuestionnaire = null;
		User selectedUser = null;
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		questionnaires = qService.findAllBeforeDate(date);
		
		Integer chosenQ = null;
		if (request.getParameterMap().containsKey("questionnaireid") && request.getParameter("questionnaireid") != ""
				&& !request.getParameter("questionnaireid").isEmpty()) {
			try {
				chosenQ = Integer.parseInt(request.getParameter("questionnaireid"));
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Questionnaire parameters");
				return;
			}
		}
		if (chosenQ != null)
			selectedQuestionnaire = qService.findById(chosenQ);
		if (chosenQ == null | selectedQuestionnaire == null)
			selectedQuestionnaire = qService.findDefault();
		
		Integer chosenU = null;
		if (request.getParameterMap().containsKey("userid") && request.getParameter("userid") != ""
				&& !request.getParameter("userid").isEmpty()) {
			try {
				chosenU = Integer.parseInt(request.getParameter("userid"));
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User parameters");
				return;
			}
		}
		if (chosenU != null) {
			selectedUser = uService.findById(chosenU);
			for(Answer a : selectedUser.getAnswers()) {
				if(a.getQuestionnaire().getId() == selectedQuestionnaire.getId())
					answers.add(a);
			}
		}
			
		
		List<Submitstatus> submitstatuses = null;
		submitstatuses = selectedQuestionnaire.getSubmitstatuses();
		
		List<User> users = new ArrayList<User>();
		for(Submitstatus s : submitstatuses) {
			if(s.getStatus().compareTo("Submitted") == 0)
				if(!users.contains(s.getUser()))
				users.add(s.getUser());
		}
		
		
		String path = "/WEB-INF/Inspection.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questionnaires", questionnaires);
		if (selectedQuestionnaire != null)
			ctx.setVariable("selectedQuestionnaire", selectedQuestionnaire);
		if (selectedUser != null) {
			ctx.setVariable("selectedUser", selectedUser);
			ctx.setVariable("answers", answers);
		}
			
		ctx.setVariable("users", users);
		ctx.setVariable("submitstatuses", submitstatuses);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

}
