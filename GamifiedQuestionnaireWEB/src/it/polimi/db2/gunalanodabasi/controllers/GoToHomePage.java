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
import it.polimi.db2.gunalanodabasi.services.ProductService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.SessionService;

import java.util.List;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.album.services/ProductService")
	private ProductService pService;
	@EJB(name = "it.polimi.db2.album.services/QuestionnaireService")
	private QuestionnaireService qService;

	public GoToHomePage() {
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
		SessionService SessionService = (SessionService) request.getSession().getAttribute("sessionService");
		User user = SessionService.getUser();
		
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		
		Questionnaire q = null;
		Product p = null;
		q = qService.findByDate(date);
		if(q != null)
			p = q.getProduct();

		String path = "/WEB-INF/Home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("user", user);
		if (p != null) {
			ctx.setVariable("product", p);
		}
		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
