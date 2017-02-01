package org.qqq175.blackjack.controller;

import org.qqq175.blackjack.StringConstant;

/**
 * Command path parcer
 * 
 * @author qqq175
 */
public class CommandParser {
	public static final String DELIMETER = "/";

	public CommandParser() {
	}

	/**
	 * parse request path (without servlet context path) to CommandContext
	 * 
	 * @param path
	 * @return
	 */
	public CommandContext parse(String path) {
		String[] queryParts = path.trim().replaceFirst("^" + DELIMETER, "").split(DELIMETER);
		String scope = null;
		String action = null;
		switch (queryParts.length) {
		case 0:
			/* if index - default action(/main/index) */
			scope = StringConstant.SCOPE_DEFAULT;
			action = StringConstant.ACTION_DEFAULT;
			break;
		case 1:
			/* if one param - default scope(/main/param) */
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
	 * 
	 * @author qqq175
	 *
	 */
	public static class CommandContext {

		public CommandContext(boolean isEmpty) {
			super();
			this.isEmpty = isEmpty;
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((action == null) ? 0 : action.hashCode());
			result = prime * result + (isEmpty ? 1231 : 1237);
			result = prime * result + ((scope == null) ? 0 : scope.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CommandContext other = (CommandContext) obj;
			if (action == null) {
				if (other.action != null)
					return false;
			} else if (!action.equals(other.action))
				return false;
			if (isEmpty != other.isEmpty)
				return false;
			if (scope == null) {
				if (other.scope != null)
					return false;
			} else if (!scope.equals(other.scope))
				return false;
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("CommandContext [scope=").append(scope).append(", action=").append(action).append(", isEmpty=").append(isEmpty)
					.append("]");
			return builder.toString();
		}
	}
}
