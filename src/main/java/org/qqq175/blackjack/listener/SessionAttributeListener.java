package org.qqq175.blackjack.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.game.impl.BlackJackGame;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.GamePool;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Application Lifecycle Listener implementation class SessionAttributeListener
 *
 */
@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {
	private static Logger log = LogManager.getLogger(SessionAttributeListener.class);

	/**
	 * Default constructor.
	 */
	public SessionAttributeListener() {

	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		log.debug(event.getName() + " added");
		String attrName = event.getName();
		if (attrName.equals(StringConstant.ATTRIBUTE_USER)) {
			User value = (User) event.getValue();
			log.debug(value.getAccountBalance() + "->" + UserPool.getInstance().get(value.getId()));
			UserPool.getInstance().put(value);
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		log.debug(event.getName() + " removed");
		String attrName = event.getName();
		if (attrName.equals(StringConstant.ATTRIBUTE_USER)) {
			User value = (User) event.getValue();
			if (value != null) {
				BlackJackGame game = GamePool.getInstance().get(value.getId());
				game.leave(value);
				GamePool.getInstance().remove(value.getId());
				UserPool.getInstance().remove(value.getId());
			}
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		log.debug(event.getName() + " replaced");
		String attrName = event.getName();
		if (attrName.equals(StringConstant.ATTRIBUTE_USER)) {
			/* getting new user value */
			User value = (User) event.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);

			UserPool.getInstance().replace(value);
		}
	}

}
