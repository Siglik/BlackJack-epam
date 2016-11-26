package org.qqq175.blackjack.persistence.dto;

import org.qqq175.blackjack.persistence.dto.id.EntityId;

public abstract class Entity<ID extends EntityId> {

	private ID id;

	public ID getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ID id) {
		this.id = id;
	}
}
