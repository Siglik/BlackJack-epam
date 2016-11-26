package org.qqq175.blackjack.service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionRequestContent {
	private Map<String, Object> requestAttributes;
	private Map<String, String[]> requestParameters;
	private Map<String, Object> sessionAttributes;
	boolean isRequestChanged, isSessionChanged;
	boolean isSessionInvalidated;

	/**
	
	 */
	public SessionRequestContent(HttpServletRequest request) {
		this.requestAttributes = new HashMap<String, Object>();
		this.requestParameters = new HashMap<String, String[]>();
		this.sessionAttributes = new HashMap<String, Object>();
		extractValues(request);
	}

	private void extractValues(HttpServletRequest request) {
		isRequestChanged = false;
		isSessionChanged = false;
		isSessionInvalidated = false;
		Enumeration<String> attrNames = request.getAttributeNames();
		// requestAttributes.clear();
		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			requestAttributes.put(attrName, request.getAttribute(attrName));
		}

		requestParameters = request.getParameterMap();

		sessionAttributes.clear();
		HttpSession session = request.getSession();
		attrNames = session.getAttributeNames();
		// requestAttributes.clear();
		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			sessionAttributes.put(attrName, session.getAttribute(attrName));
		}
	}

	// метод добавления в запрос данных для передачи в jsp
	public void insertAttributes(HttpServletRequest request) {
		if (isRequestChanged) {
			requestAttributes.forEach((name, value) -> {
				request.setAttribute(name, value);
			});
		}
		if (!isSessionInvalidated) {
			if (isSessionChanged) {
				HttpSession session = request.getSession();
				sessionAttributes.forEach((name, value) -> {
					session.setAttribute(name, value);
				});
			}
		} else {
			request.getSession().invalidate();
		}
	}

	/**
	 * @return the requestAttributes
	 */
	public Set<String> getRequestAttributesNames() {
		return requestAttributes.keySet();
	}

	/**
	 * @return the sessionAttributes
	 */
	public Set<String> getSessionAttributesNames() {
		return sessionAttributes.keySet();
	}

	/**
	 * @return the requestParameters
	 */
	public Map<String, String[]> getRequestParameters() {
		return requestParameters;
	}

	/**
	 * @return the requestParameters
	 */
	public String getParameter(String name) {
		String[] params = requestParameters.get(name);
		if (params != null) {
			return params[0];
		} else {
			return "";
		}
	}

	/**
	 * @return the requestParameters
	 */
	public String[] getParameters(String name) {
		return requestParameters.get(name);
	}
	/**
	 * 
	 */
	public void setAttribute(String name, Object value) {
		isRequestChanged = true;
		requestAttributes.put(name, value);
	}

	/**
	 * 
	 */
	public void setSessionAttribute(String name, Object value) {
		isSessionChanged = true;
		sessionAttributes.put(name, value);
	}

	/**
	 * 
	 */
	public Object getAttribute(String name) {
		return requestAttributes.get(name);
	}

	/**
	 * 
	 */
	public Object getSessionAttribute(String name) {
		return sessionAttributes.get(name);
	}

	/**
	 * @return the isSessionInvalidated
	 */
	public boolean isSessionInvalidated() {
		return isSessionInvalidated;
	}

	/**
	 * @param isSessionInvalidated
	 *            the isSessionInvalidated to set
	 */
	public void setSessionInvalidated(boolean isSessionInvalidated) {
		this.isSessionInvalidated = isSessionInvalidated;
	}

}
