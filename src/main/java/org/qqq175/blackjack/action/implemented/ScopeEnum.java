package org.qqq175.blackjack.action.implemented;

/**
 * contains list of available scopes
 * @author qqq175
 */
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
