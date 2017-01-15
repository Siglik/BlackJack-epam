package org.qqq175.blackjack.action.implemented.main;

import static org.qqq175.blackjack.action.ActionResult.ActionType.REDIRECT;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;
import org.qqq175.blackjack.logic.main.LocaleLogic;
import org.qqq175.blackjack.persistence.dao.util.Settings;

public class SetLocaleAction implements Action {

	public SetLocaleAction() {
	}

	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		String localeString = request.getParameter(StringConstant.PARAMETER_LOCALE);
		LocaleLogic lLogic = new LocaleLogic();
		Locale locale = lLogic.getLocaleByString(localeString);

		request.getSession().setAttribute(StringConstant.ATTRIBUTE_LOCALE, locale);

		Cookie cookie = new Cookie(StringConstant.COOKIE_LOCALE, locale.getLanguage());
		cookie.setMaxAge(7 * 24 * 60 * 60);
		response.addCookie(cookie);

		return new ActionResult(REDIRECT, Settings.getInstance().getContextPath());
	}

}
