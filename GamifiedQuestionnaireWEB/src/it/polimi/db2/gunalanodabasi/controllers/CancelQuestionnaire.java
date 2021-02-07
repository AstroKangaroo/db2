package it.polimi.db2.gunalanodabasi.controllers;

import java.io.*;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import it.polimi.db2.gunalanodabasi.entities.Questionnaire;
import it.polimi.db2.gunalanodabasi.entities.User;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.SessionService;
import it.polimi.db2.gunalanodabasi.services.SubmitstatusService;


@WebServlet("/CancelQuestionnaire")
public class CancelQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qrService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/SubmitstatusService")
	private SubmitstatusService ssService;

	public CancelQuestionnaire() {
		super();
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
		
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);		
		Questionnaire qr = null;
		qr = qrService.findByDate(date);

		
		ssService.createSubmitstatus(u.getId(), qr.getId(), "Cancelled");
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToHomePage";
		response.sendRedirect(path);
	}
}