/**
 * 
 */
package org.qqq175.blackjack.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action interfacer for Command pattern of service layer
 * @author qqq175
 */
public interface Action {
	/**
	 * execute action
	 * @param request
	 * @param response
	 * @return
	 */
	ActionResult execute(HttpServletRequest request, HttpServletResponse response);
}