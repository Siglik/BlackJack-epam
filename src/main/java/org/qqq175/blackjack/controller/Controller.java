package org.qqq175.blackjack.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionFactory;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.action.implemented.ActionFactoryImpl;
import org.qqq175.blackjack.persistence.dao.util.Settings;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = { "/$/*" })
@MultipartConfig(fileSizeThreshold = 1024 * 512, // 512 KB
		maxFileSize = 1024 * 512, // 512 KB
		maxRequestSize = 1024 * 528 // 520 KB (512 KB for image + 16 KB for
									// other data
)
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
		String query = request.getPathInfo();

		CommandParser cp = new CommandParser();
		CommandParser.CommandContext comandContext = cp.parse(query);

		if (!comandContext.isEmpty()) {
			ActionFactory actionFactory = new ActionFactoryImpl();
			Action concreteAction = actionFactory.defineAction(comandContext.getScope(), comandContext.getAction());

			ActionResult result = concreteAction.execute(request);

			switch (result.getType()) {
			case FORWARD:
				RequestDispatcher dispatcher = request.getRequestDispatcher(result.getContent());
				dispatcher.forward(request, response);
				break;
			case REDIRECT:
				response.sendRedirect(result.getContent());
				break;
			case SENDERROR:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, result.getContent());
				break;
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
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
		Settings.getInstance().setContextPath(this.getServletContext().getContextPath());
	}

}
