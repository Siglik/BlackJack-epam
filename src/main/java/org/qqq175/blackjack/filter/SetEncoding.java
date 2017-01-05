package org.qqq175.blackjack.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class CheckAutorized
 */
@WebFilter("/")
public class SetEncoding implements Filter {
	//private Set<String> adminCommands, dispCommands;
	

	/**
	 * Default constructor.
	 */
	public SetEncoding() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		response.setLocale(new Locale("ru-RU"));
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
//		HttpServletRequest request = (HttpServletRequest) req;
//		HttpServletResponse response = (HttpServletResponse) resp;
//		HttpSession session = request.getSession();
//		User user = (User) session.getAttribute("airlineUser");
//		String url = request.getServletPath();
//		DispatcherType dispType = request.getDispatcherType();
//		String action = request.getParameter("action");
//		String context = request.getContextPath();
//		// block direct requests
//		if (dispType == DispatcherType.REQUEST && (url.endsWith(".jsp"))) {
//			response.sendRedirect(context);
//		} else {
//			if (action != null) {
//				action = action.toUpperCase();
//
//				System.out.println("FILTER DOING CHECK : " + context + " " + url + " ~ " + user + " | " + action);
//
//				if (!((action.equals("LOGIN")) || (action.equals("")) || (!action.equals("NONE")))) {
//					System.out.println("001");
//					if (!(user.getType() == User.UserType.ADMIN && adminCommands.contains(action))) {
//						System.out.println("002");
//						response.sendRedirect(context);
//					} else if (!(user.getType() != User.UserType.DISPATCHER && dispCommands.contains(action))) {
//						System.out.println("003");
//						response.sendRedirect(context);
//					}
//				}
//
//			}
//		}
//
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
//		adminCommands = new HashSet<>();
//		dispCommands = new HashSet<>();
//		adminCommands.add("NONE");
//		adminCommands.add("UNKNOWN");
//		adminCommands.add("LOGIN");
//		adminCommands.add("LOG_OUT");
//		adminCommands.add("SHOW_FLIGHTS");
//
//		dispCommands.addAll(adminCommands);
//
//		adminCommands.add("SHOW_ROUTES");
//		adminCommands.add("SHOW_CITIES");
//		adminCommands.add("SHOW_HUBS");
//		adminCommands.add("SHOW_EMPLOYEES");
//		adminCommands.add("NEW_CITY");
//		adminCommands.add("DELETE_CITY");
//		adminCommands.add("NEW_HUB");
//		adminCommands.add("DELETE_HUB");
//		adminCommands.add("NEW_ROUTE");
//		adminCommands.add("DELETE_ROUTE");
//		adminCommands.add("NEW_FLIGHT");
//		adminCommands.add("DELETE_FLIGHT");
//
//		dispCommands.add("EDIT_FLIGHT_CREW");
//		dispCommands.add("SAVE_CREW");
//		dispCommands.add("NEW_EMPLOYEE");
//		dispCommands.add("DELETE_EMPLOYEE");
	}

}
