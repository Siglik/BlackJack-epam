/**
 * 
 */
package org.qqq175.blackjack.action;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qqq175
 *
 */
public interface Action {
	ActionResult execute(HttpServletRequest request);
}