package org.qqq175.blackjack.service;

/**
 * Data class used to represent result of action
 * @author qqq175
 *
 */
public class ActionResult {
	private ActionType type;
	/**
	 * additional info, such as redirect(forward) path, error text, attribute parameter(for JSON) 
	 */
	private String content;
	
	/**
	 * 
	 * @param type - action type
	 * @param content - additional info, such as redirect(forward) path, error text, attribute parameter(for JSON) 
	 */
	public ActionResult(ActionType type, String content) {
		this.type = type;
		this.content = content;
	}

	/**
	 * avaliable servlet action types
	 * @author qqq175
	 */
	public enum ActionType {
		FORWARD, REDIRECT, SENDERROR, JSON;
	}

	/**
	 * @return the type
	 */
	public ActionType getType() {
		return type;
	}

	/**
	 * @return the content (such as redirect(forward) path, error text, attribute parameter(for JSON))
	 */
	public String getContent() {
		return content;
	}
}
