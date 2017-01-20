package org.qqq175.blackjack.persistence.entity.id;

import java.io.Serializable;

public class EntityId implements Comparable<EntityId>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final long VALUE;

	public EntityId(long value) {
		this.VALUE = value;
	}

	public EntityId(EntityId entityId) {
		this.VALUE = entityId.getValue();
	}

	/**
	 * @return the value
	 */
	final public long getValue() {
		return VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (VALUE ^ (VALUE >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityId other = (EntityId) obj;
		if (VALUE != other.VALUE)
			return false;
		return true;
	}

	@Override
	final public int compareTo(EntityId anotherId) {
		return Long.compare(this.VALUE, anotherId.VALUE);
	}
}
