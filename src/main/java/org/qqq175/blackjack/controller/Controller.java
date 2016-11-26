package org.qqq175.blackjack.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.service.SessionRequestContent;
import org.qqq175.blackjack.service.action.Action;
import org.qqq175.blackjack.service.action.implemented.ActionEnum;
import org.qqq175.blackjack.service.action.implemented.ActionFactory;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/")
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

		// response.setLocale(new Locale("ru-RU"));
		// response.setCharacterEncoding("UTF-8");
		// request.setCharacterEncoding("UTF-8");

		SessionRequestContent sessionRequestContent = new SessionRequestContent(request);
		Action action = ActionEnum.NONE.getAction();

		sessionRequestContent = action.execute(sessionRequestContent);
		sessionRequestContent.insertAttributes(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher((String) request.getAttribute("page"));
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.setLocale(new Locale("ru-RU"));
		// response.setCharacterEncoding("UTF-8");
		// request.setCharacterEncoding("UTF-8");

		ActionFactory actionFactory = new ActionFactory();
		SessionRequestContent sessionRequestContent = new SessionRequestContent(request);
		Action action = actionFactory.defineAction(sessionRequestContent);

		sessionRequestContent = action.execute(sessionRequestContent);
		sessionRequestContent.insertAttributes(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher((String) request.getAttribute("page"));
		dispatcher.forward(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
		Logger.getInstance().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		Settings.getInstance().setRealPath(this.getServletContext().getRealPath("/"));
	}

}
