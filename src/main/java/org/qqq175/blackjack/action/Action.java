/**
 * 
 */
package org.qqq175.blackjack.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qqq175
 *
 */
public interface Action {
	ActionResult execute(HttpServletRequest request, HttpServletResponse response);
}