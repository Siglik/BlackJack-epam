package org.qqq175.blackjack.action.implemented.main;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.action.Action;
import org.qqq175.blackjack.action.ActionResult;

/**
 * log some data about new visitors (got from client-side script and add some server data info) and save cookie for prevent "overlogging"
 * @author qqq175
 *
 */
public class LogVisitorInfoAction implements Action {
	private static final int COOKIE_AGE = 8 * 60 * 60;
	private static Logger log = LogManager.getLogger(LogVisitorInfoAction.class);

	@SuppressWarnings("unchecked")
	@Override
	public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String info = request.getParameter("info");
		if (info != null) {
			Cookie[] cookies = request.getCookies();
			boolean cookieFound = false;
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(StringConstant.COOKIE_VISITED)) {
						cookieFound = true;
						break;
					}
				}
			}
			if (!cookieFound) {
				JSONParser parser = new JSONParser();
				try {
					JSONObject infoJSON = (JSONObject) parser.parse(info);
					infoJSON.put("ip", request.getRemoteAddr());
					infoJSON.put("sessionId", request.getSession().getId());
					infoJSON.put("forwardIp", request.getHeader("X-FORWARDED-FOR"));
					infoJSON.put("hostName", request.getRemoteHost());
					infoJSON.put("lang", request.getLocale().getLanguage());
					log.log(Level.toLevel("VISITINFO", Level.INFO), infoJSON.toJSONString() + "\n");

					Cookie cookie = new Cookie(StringConstant.COOKIE_VISITED, "true");
					cookie.setMaxAge(COOKIE_AGE);
					response.addCookie(cookie);

					result.put("status", "OK");
				} catch (ParseException e) {
					log.log(Level.toLevel("VISITINFO", Level.INFO), "unable to log visitor info", e);
				}
			} else {
				result.put("status", "NO ACTION");
			}
		}
		request.setAttribute(StringConstant.ATTRIBUTE_JSON, result);
		return new ActionResult(ActionResult.ActionType.JSON, StringConstant.ATTRIBUTE_JSON);
	}

}
