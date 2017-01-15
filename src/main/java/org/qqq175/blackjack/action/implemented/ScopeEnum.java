package org.qqq175.blackjack.action.implemented;

public enum ScopeEnum {
	MAIN {
		{
			this.scope = MainActionEnum.class;
		}
	},
	GAME {
		{
			this.scope = GameActionEnum.class;
		}
	},
	CHAT {
		{
			this.scope = ChatActionEnum.class;
		}
	},
	ADMIN {
		{
			this.scope = AdminActionEnum.class;
		}
	},
	PLAYER {
		{
			this.scope = PlayerActionEnum.class;
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
