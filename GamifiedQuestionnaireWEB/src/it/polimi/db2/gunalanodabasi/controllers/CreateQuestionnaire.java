package it.polimi.db2.gunalanodabasi.controllers;

import java.io.*;
import java.sql.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import it.polimi.db2.gunalanodabasi.services.QuestionService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;


@WebServlet("/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionService")
	private QuestionService quService;

	public CreateQuestionnaire() {
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
		String error = ""; 
		Integer productId = null;
		try {
			productId = Integer.parseInt(request.getParameter("productId"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Product parameters");
			return;
		}
		String[] Questions = request.getParameterValues("question");
		Date date = Date.valueOf(request.getParameter("date"));
		date.setTime(date.getTime() + 3600000 /* 1 hour */);
		
		if( qService.findByDate(date) != null) {
			error = "A questionnaire already exists for the date!";
			String path = "/WEB-INF/Creation.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("error", error);
			templateEngine.process(path, ctx, response.getWriter());
		}
		else {
			int QuestionNum = Integer.parseInt(request.getParameter("questionnum"));
			
			if (date == null | Questions.length != QuestionNum) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Questionnaire parameters");
				return;
			}
			for (int i = 0; i < QuestionNum; i++) {
				if (Questions[i] == null) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing questions");
					return;
				}
			}
			int Questionnaireid = qService.createQuestionnaire(date, productId);
			for (int i = 0; i < QuestionNum; i++) {
				quService.createQuestion(i, Questions[i], Questionnaireid);
			}
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/GoToAdminHomePage";
			response.sendRedirect(path);
		}
	}
}