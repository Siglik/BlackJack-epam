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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.logic.main.LocaleLogic;

/**
 * Servlet Filter implementation class SetLocaleFilter
 * set session lovale based on cookies or browser settings if it is supported. Else set default.
 */
@WebFilter({ "/$/*", "/api/*" })
public class SetLocaleFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		Locale locale = (Locale) session.getAttribute(StringConstant.ATTRIBUTE_LOCALE);

		if (locale == null) {
			String localeStr = "";
			
			//search locale cookies
			Cookie[] cookies = ((HttpServletRequest) request).getCookies();
			Cookie localeCookie = null;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(StringConstant.COOKIE_LOCALE)) {
						localeCookie = cookie;
						break;
					}
				}
			}
			if (localeCookie != null) {
				localeStr = localeCookie.getValue();
			} else {
				//if no cookie - get browser lang
				String browserLocale = request.getLocale().getLanguage();
				if (browserLocale != null) {
					localeStr = browserLocale;
				}
			}
			
			//store locale to session
			LocaleLogic localeLogic = new LocaleLogic();
			locale = localeLogic.getLocaleByString(localeStr);
			session.setAttribute(StringConstant.ATTRIBUTE_LOCALE, locale);
		}
		response.setLocale(locale);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
