/**
 * 
 */
package org.qqq175.blackjack.service.action;

import org.qqq175.blackjack.service.SessionRequestContent;

/**
 * @author qqq175
 *
 */
public interface Action {
	SessionRequestContent execute(SessionRequestContent requestContent);
	SessionRequestContent execute(HttpServletRequest requestContent);
}
