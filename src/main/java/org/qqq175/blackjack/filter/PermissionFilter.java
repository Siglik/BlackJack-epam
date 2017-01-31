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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.implemented.GameActionEnum;
import org.qqq175.blackjack.controller.CommandParser;
import org.qqq175.blackjack.logic.player.ModifyUserLogic;
import org.qqq175.blackjack.persistence.dao.util.JSPPathManager;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Servlet Filter implementation class PermissionFilter
 * Check user type and ban state, compare user's permission with requested action scope
 * and prevent unautorized access.
 */
@WebFilter({ "/$/*" })
public class PermissionFilter implements Filter {
	private static final String BANNED = "You account is banned.";
	private static final String FORBIDDEN_ACTION_GUEST = "You do not have permission to perform this action. Please log in with administrator account and try again.";
	private static final String FORBIDDEN_ACTION = "You do not have permission to perform this action. Please log in and try again.";
	private static Logger log = LogManager.getLogger(ModifyUserLogic.class);

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
		if (query == null) {
			query = ((HttpServletRequest) request).getServletPath();
		}

		CommandParser cp = new CommandParser();
		CommandParser.CommandContext comandContext = cp.parse(query);
		User userFromPool = user;

		if (user != null) {
			userFromPool = UserPool.getInstance().get(user.getId());
			if (userFromPool != null) {
				if (userFromPool.isActive()) {
					if (userFromPool.getType() == User.Type.ADMIN) {
						//admin - grant access to all
						grantAccess = true;
					} else if (userFromPool.getType() == User.Type.PLAYER) {
						//user - grant access to all except ADMIN scope
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
				// this may be happen only on tomcat cotext reload
				// session will be serialized, but user pool don't
				grantAccess = false;
				session.invalidate();
				session = ((HttpServletRequest) request).getSession();
			}
		} else {
			//grant access for all for scope
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
				//if forbidden action for logge user
				if (userFromPool == null) {
					session.setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE,
							FORBIDDEN_ACTION);
				} else {
					//if GUEST
					session.setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE,
							FORBIDDEN_ACTION_GUEST);
				}
				((HttpServletResponse) response).sendRedirect(Settings.getInstance().getContextPath() + JSPPathManager.getProperty("command.index"));
				if (!comandContext.getAction().equalsIgnoreCase(GameActionEnum.GETSTATE.toString())) {
					log.warn(request.getRemoteAddr() + " | " + session.getId() + " - User type "
							+ (userFromPool != null ? userFromPool.getType().name() : "GUEST") + " isn't allowed to perform action "
							+ comandContext.getAction().toUpperCase() + " in scope " + comandContext.getScope().toUpperCase());
				}
			} else {
				//if banned - invalidate session
				session.invalidate();
				HttpSession newSession = ((HttpServletRequest) request).getSession();
				newSession.setAttribute(StringConstant.ATTRIBUTE_POPUP_MESSAGE, BANNED);
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
