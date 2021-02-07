package it.polimi.db2.gunalanodabasi.controllers;

import java.io.*;
import java.sql.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db2.gunalanodabasi.services.QuestionService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;


@WebServlet("/DeleteQuestionnaire")
@MultipartConfig
public class DeleteQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/QuestionnaireService")
	private QuestionnaireService qService;

	public DeleteQuestionnaire() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int questionnaireid = Integer.parseInt(request.getParameter("questionnaireid"));
		qService.deleteQuestionnaire(questionnaireid);
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToDeletionPage";
		response.sendRedirect(path);
	}
}