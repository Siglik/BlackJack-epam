package org.qqq175.blackjack.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.pool.UserPool;

/**
 * Application Lifecycle Listener implementation class SessionAttributeListener
 *
 */
@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {

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
		System.out.println(event.getName() + " added");
		String attrName = event.getName();
		if (attrName.equals(StringConstant.ATTRIBUTE_USER)) {
			User value = (User) event.getValue();
			UserPool.getInstance().put(value);
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		System.out.println(event.getName() + " removed");
		String attrName = event.getName();
		if (attrName.equals(StringConstant.ATTRIBUTE_USER)) {
			User value = (User) event.getValue();
			if (value != null) {
				UserPool.getInstance().remove(value.getId());
			}
		}
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		System.out.println(event.getName() + " replaced");
		String attrName = event.getName();
		if (attrName.equals(StringConstant.ATTRIBUTE_USER)) {
			User value = (User) event.getValue();
			UserPool.getInstance().replace(value);
		}
	}

}
