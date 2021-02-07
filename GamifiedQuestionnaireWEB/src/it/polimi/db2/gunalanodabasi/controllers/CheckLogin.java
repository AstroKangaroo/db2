package it.polimi.db2.gunalanodabasi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.gunalanodabasi.services.AdminService;
import it.polimi.db2.gunalanodabasi.services.SessionService;
import it.polimi.db2.gunalanodabasi.services.UserService;
import it.polimi.db2.gunalanodabasi.entities.Admin;
import it.polimi.db2.gunalanodabasi.entities.Statisticalanswer;
import it.polimi.db2.gunalanodabasi.entities.User;
import it.polimi.db2.gunalanodabasi.exceptions.CredentialsException;
import javax.persistence.NonUniqueResultException;

import javax.naming.*;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/UserService")
	private UserService usrService;
	@EJB(name = "it.polimi.db2.gunalanodabasi.services/AdminService")
	private AdminService admService;

	public CheckLogin() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// obtain and escape params
		String usrn = null;
		String pwd = null;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		User user = null;
		Admin admin = null;

		try {
			admin = admService.checkCredentials(usrn, pwd);
			user = usrService.checkCredentials(usrn, pwd);
		} catch (CredentialsException | NonUniqueResultException e) {
			if (admin == null && user == null) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
				return;
			}
		}

		String path;
		if(admin != null || user != null)
		{
			SessionService sService = null;
			try {
				/*
				 * We need one distinct EJB for each user. Get the Initial Context for the JNDI
				 * lookup for a local EJB. Note that the path may be different in different EJB
				 * environments. In IntelliJ use: ic.lookup(
				 * "java:/openejb/local/ArtifactFileNameWeb/ArtifactNameWeb/QueryServiceLocalBean"
				 * );
				 */
				InitialContext ic = new InitialContext();
				// Retrieve the EJB using JNDI lookup
				sService = (SessionService) ic.lookup("java:/openejb/local/SessionServiceLocalBean");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(user != null) {
				sService.saveUser(user);	
			}
				
			
			request.getSession().setAttribute("sessionService", sService);
		}
		
		if (admin != null) {
			request.getSession().setAttribute("admin", admin);
			path = getServletContext().getContextPath() + "/GoToAdminHomePage";
			response.sendRedirect(path);

		} else if (user != null) {
			request.getSession().setAttribute("user", user);
			path = getServletContext().getContextPath() + "/GoToHomePage";
			usrService.updateLastLogin(user.getId());
			response.sendRedirect(path);

		} else {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect username or password");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		}

	}

	public void destroy() {
	}
}