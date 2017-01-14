package org.qqq175.blackjack.persistence.dao;

import java.math.BigDecimal;

import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.id.AccountOperationId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public interface AccountOperationDAO extends EntityDAO<AccountOperation, AccountOperationId> {
	BigDecimal calcTotal(AccountOperation.Type type);

	BigDecimal calcTotal(AccountOperation.Type type, UserId userId);
}
