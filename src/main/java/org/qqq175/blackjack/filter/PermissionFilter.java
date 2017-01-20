package org.qqq175.blackjack.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.controller.CommandParser;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Servlet Filter implementation class PermissionChecker
 */
@WebFilter("/$/*")
public class PermissionFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public PermissionFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean grantAccess = false;
		boolean banned = false;
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute(StringConstant.ATTRIBUTE_USER);
		String query = ((HttpServletRequest) request).getPathInfo();

		CommandParser cp = new CommandParser();
		CommandParser.CommandContext comandContext = cp.parse(query);

		if (user != null) {
			if (user.isActive()) {
				if (user.getType() == User.Type.ADMIN) {
					grantAccess = true;
				} else if (user.getType() == User.Type.PLAYER) {
					if (comandContext.getScope().equalsIgnoreCase("admin")) {
						grantAccess = false;
					} else {
						grantAccess = true;
					}
				}
			} else {
				grantAccess = false;
				banned = true;
			}
		} else {
			if (comandContext.getScope().equalsIgnoreCase("main")) {
				grantAccess = true;
			} else {
				grantAccess = false;
			}
		}

		if (grantAccess) {
			chain.doFilter(request, response);
		} else {
			if (!banned) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
						"User type " + (user != null ? user.getType().name() : "GUEST") + " isn't allowed to perform action "
								+ comandContext.getAction().toUpperCase() + " in scope " + comandContext.getScope().toUpperCase());
			} else {
				session.invalidate();
				UserPool.getInstance().remove(user.getId());
				HttpSession newSession = ((HttpServletRequest) request).getSession();
				newSession.setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, "You account is banned.");
				((HttpServletResponse) response).sendRedirect(Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.index"));
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
