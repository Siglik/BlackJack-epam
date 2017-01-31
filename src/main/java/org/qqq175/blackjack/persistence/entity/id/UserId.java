package org.qqq175.blackjack.persistence.entity.id;

/**
 * Id for User
 * @author qqq175
 */
public class UserId extends EntityId {
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
