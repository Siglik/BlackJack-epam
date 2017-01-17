package org.qqq175.blackjack.persistence.entity;

import org.qqq175.blackjack.persistence.entity.id.EntityId;

public abstract class Entity<K extends EntityId> {

	private K id;

	public K getId() {
		System.out.println("Id getter^ " + id);
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(K id) {
		this.id = id;
		System.out.println("Id SETTER^ " + id);
	}
}
