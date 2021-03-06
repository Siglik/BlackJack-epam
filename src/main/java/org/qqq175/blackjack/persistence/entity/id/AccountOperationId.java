package org.qqq175.blackjack.persistence.entity.id;

/**
 * Id for Account operation
 * @author qqq175
 */
public class AccountOperationId extends EntityId {

	private static final long serialVersionUID = 1L;

	public AccountOperationId(EntityId entityId) {
		super(entityId);
	}

	public AccountOperationId(long value) {
		super(value);
	}
}
