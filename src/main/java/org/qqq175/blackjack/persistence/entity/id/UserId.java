package org.qqq175.blackjack.persistence.entity.id;

public class UserId extends EntityId {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		System.out.println("finalize user " + this.getValue());
		super.finalize();
	}

}
