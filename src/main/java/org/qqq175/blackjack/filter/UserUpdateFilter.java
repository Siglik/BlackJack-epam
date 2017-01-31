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
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Servlet Filter implementation class UserUpdateFilter
 * Updates user in session from user pool if changed
 */
@WebFilter({ "/$/*", "/game/*" })
public class UserUpdateFilter implements Filter {
	private static Logger log = LogManager.getLogger(UserUpdateFilter.class);

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
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute(StringConstant.ATTRIBUTE_USER);

		if (user != null) {
			User userFromPool = UserPool.getInstance().get(user.getId());
			//if user from pool is changed - update session value
			if (userFromPool != null && !userFromPool.equals(user)) {
				log.debug(user.getAccountBalance() + "->" + userFromPool.getAccountBalance());
				session.setAttribute(StringConstant.ATTRIBUTE_USER, userFromPool);
			}
		}

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
