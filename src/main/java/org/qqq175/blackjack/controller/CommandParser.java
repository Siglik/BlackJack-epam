package org.qqq175.blackjack.controller;

import org.qqq175.blackjack.StringConstant;

/**
 * Command path parcer
 * @author qqq175
 */
public class CommandParser {
	public static final String DELIMETER = "/";

	public CommandParser() {
	}

	/**
	 * parse request path (without servlet context path) to CommandContext
	 * @param path
	 * @return
	 */
	public CommandContext parse(String path) {
		String[] queryParts = path.trim().replaceFirst("^" + DELIMETER, "").split(DELIMETER);
		String scope = null;
		String action = null;
		switch (queryParts.length) {
		case 0:
			/*if index - default action(/main/index)*/
			scope = StringConstant.SCOPE_DEFAULT;
			action = StringConstant.ACTION_DEFAULT;
			break;
		case 1:
			/*if one param - default scope(/main/param)*/
			scope = StringConstant.SCOPE_DEFAULT;
			action = !queryParts[0].isEmpty() ? queryParts[0] : StringConstant.ACTION_DEFAULT;
			break;
		case 2:
			scope = queryParts[0];
			action = queryParts[1];
			break;
		default:
			return new CommandContext(true);
		}

		return new CommandContext(scope, action, false);
	}

	/**
	 * Data class that contains Scope and Action strings
	 * @author qqq175
	 *
	 */
	public class CommandContext {

		public CommandContext(boolean isSuccess) {
			super();
			this.isEmpty = isSuccess;
			this.scope = null;
			this.action = null;
		}

		public CommandContext(String scope, String action, boolean isEmpty) {
			super();
			this.scope = scope;
			this.action = action;
			this.isEmpty = isEmpty;
		}

		public String getScope() {
			return scope;
		}

		public String getAction() {
			return action;
		}

		public boolean isEmpty() {
			return isEmpty;
		}

		private String scope;
		private String action;
		private boolean isEmpty;
	}
}
