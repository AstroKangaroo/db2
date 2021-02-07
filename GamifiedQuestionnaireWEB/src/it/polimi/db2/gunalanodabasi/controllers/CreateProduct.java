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
import it.polimi.db2.gunalanodabasi.services.ProductService;


@WebServlet("/CreateProduct")
@MultipartConfig
public class CreateProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/ProductService")
	private ProductService pService;

	public CreateProduct() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String name = request.getParameter("productname");
		
		Part imgFile = request.getPart("picture");
		InputStream imgContent = imgFile.getInputStream();
		byte[] imgByteArray = ImageUtils.readImage(imgContent);
		
		if (name == null | name.isEmpty() | imgByteArray.length == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product parameters");
			return;
		}
		
		pService.createProduct(name, imgByteArray);
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToCreationPage";
		response.sendRedirect(path);
	}
}