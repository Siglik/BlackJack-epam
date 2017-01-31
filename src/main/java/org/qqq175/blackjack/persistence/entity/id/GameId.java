package org.qqq175.blackjack.persistence.entity.id;

/**
 * Id for Game
 * @author qqq175
 */
public class GameId extends EntityId {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameId(long value) {
		super(value);
	}

	public GameId(EntityId entityId) {
		super(entityId);
	}

}
