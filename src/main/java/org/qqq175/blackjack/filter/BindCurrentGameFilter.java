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

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;

/**
 * Servlet Filter implementation class BindCurrentGameFilter
 */
@WebFilter({ "/$/player/*", "/$/admin/*", "/$/", "/$/game/game/*" })
public class BindCurrentGameFilter implements Filter {

	private static final String NONE = "NONE";
	private static final String SOLO = "SOLO";
	private static final String MULTI = "MULTI";

	/**
	 * Default constructor.
	 */
	public BindCurrentGameFilter() {
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
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute(StringConstant.ATTRIBUTE_USER);
		if (user != null) {
			BlackJackGame game = GamePool.getInstance().get(user.getId());
			if (game != null) {
				if (game.getMaxPlayers() > 1) {
					session.setAttribute(StringConstant.ATTRIBUTE_CURRENT_GAME_TYPE, MULTI);
				} else {
					session.setAttribute(StringConstant.ATTRIBUTE_CURRENT_GAME_TYPE, SOLO);
				}
			} else {
				session.setAttribute(StringConstant.ATTRIBUTE_CURRENT_GAME_TYPE, NONE);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
