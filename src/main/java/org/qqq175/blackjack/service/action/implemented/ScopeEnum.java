package org.qqq175.blackjack.service.action.implemented;

public enum ScopeEnum {
	MAIN {
		{
			this.scope = MainActionEnum.class;
		}
	},
	GAME {
		{
			this.scope = MainActionEnum.class;
		}
	},
	CHAT {
		{
			this.scope = MainActionEnum.class;
		}
	},
	ADMIN {
		{
			this.scope = MainActionEnum.class;
		}
	};
	Class<? extends Enum> scope;

	/**
	 * @return the scope
	 */
	public Class<? extends Enum> getScope() {
		return scope;
	}
}
