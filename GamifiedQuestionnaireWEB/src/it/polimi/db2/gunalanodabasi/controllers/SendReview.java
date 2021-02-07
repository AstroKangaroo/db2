package it.polimi.db2.gunalanodabasi.controllers;

import java.io.*;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.polimi.db2.gunalanodabasi.utils.*;
import it.polimi.db2.gunalanodabasi.entities.Product;
import it.polimi.db2.gunalanodabasi.entities.Questionnaire;
import it.polimi.db2.gunalanodabasi.entities.User;
import it.polimi.db2.gunalanodabasi.services.ProductService;
import it.polimi.db2.gunalanodabasi.services.QuestionnaireService;
import it.polimi.db2.gunalanodabasi.services.ReviewService;
import it.polimi.db2.gunalanodabasi.services.SessionService;


@WebServlet("/SendReview")
@MultipartConfig
public class SendReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/ReviewService")
	private ReviewService rService;
	@EJB(name = "it.polimi.db2.album.services/QuestionnaireService")
	private QuestionnaireService qService;
	

	public SendReview() {
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
		User user = SessionService.getUser();
		
		String text = request.getParameter("review");
		
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);
		java.sql.Timestamp timestamp=new java.sql.Timestamp(millis);
		timestamp.setNanos(0);
		Questionnaire q = null;
		q = qService.findByDate(date);
		Product p = q.getProduct();
		
		
		if (text == null | user == null | p == null | date == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid review parameters");
			return;
		}
		
		rService.createReview(text, timestamp, user.getId(), p.getId());
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToHomePage";
		response.sendRedirect(path);
	}
}