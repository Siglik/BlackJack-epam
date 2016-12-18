package org.qqq175.blackjack.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.service.SessionRequestContent;
import org.qqq175.blackjack.service.action.Action;
import org.qqq175.blackjack.service.action.implemented.ActionFactory;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/*")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controller() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setLocale(new Locale("ru-RU"));
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		String[] query = request.getPathInfo().trim().replaceFirst("^/", "").split("/");
		String actionScope = null;
		String action = null;
		switch (query.length) {
		case 1:
			actionScope = "main";
			action = !query[0].isEmpty() ? query[0] : "index";
			break;
		case 2:
			actionScope = query[0];
			action = query[1];
			break;
		case 0:
			actionScope = "main";
			action = "index";
			break;
		default:
			// redirect to error page
		}

		SessionRequestContent sessionRequestContent = new SessionRequestContent(request);
		ActionFactory actionFactory = new ActionFactory();
		Action concreteAction = actionFactory.defineAction(actionScope, action);

		sessionRequestContent = concreteAction.execute(sessionRequestContent);
		sessionRequestContent.insertAttributes(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher((String) request.getAttribute("page"));
		// dispatcher.forward(request, response);

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h2> Scope: " + query.length + "</h2>");
		out.println("<h2> Scope: " + actionScope + "</h2>");
		out.println("<h2> Action: " + action + "</h2>");
		out.println("</body>");
		out.println("</html>");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		// Settings.getInstance().setRealPath(this.getServletContext().getRealPath("/"));
	}

}
