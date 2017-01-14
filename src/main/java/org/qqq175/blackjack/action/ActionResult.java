package org.qqq175.blackjack.action;

public class ActionResult {
	private ActionType type;
	private String content;

	public ActionResult(ActionType type, String content) {
		this.type = type;
		this.content = content;
	}

	public enum ActionType {
		FORWARD, REDIRECT, SENDERROR;
	}

	/**
	 * @return the type
	 */
	public ActionType getType() {
		return type;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
}
