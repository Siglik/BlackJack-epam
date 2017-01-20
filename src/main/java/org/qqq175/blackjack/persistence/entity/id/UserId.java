package org.qqq175.blackjack.persistence.entity.id;

public class UserId extends EntityId {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param entityId
	 */
	public UserId(EntityId entityId) {
		super(entityId);
	}

	/**
	 * @param value
	 */
	public UserId(long value) {
		super(value);
	}
}
