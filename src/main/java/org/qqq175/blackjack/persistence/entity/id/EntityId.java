package org.qqq175.blackjack.persistence.entity.id;

public class EntityId implements Comparable<EntityId> {
	private final long value;

	public EntityId(long value) {
		this.value = value;
	}

	public EntityId(EntityId entityId) {
		this.value = entityId.getValue();
	}

	/**
	 * @return the value
	 */
	final public long getValue() {
		return value;
	}

	final public static int hashCode(long value) {
		return (int) (value ^ (value >>> 32));
	}

	@Override
	final public boolean equals(Object obj) {
		if (obj instanceof EntityId) {
			return value == ((EntityId) obj).getValue();
		}
		return false;
	}

	@Override
	final public int compareTo(EntityId anotherId) {
		return Long.compare(this.value, anotherId.value);
	}
}
