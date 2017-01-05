package org.qqq175.blackjack.persistence.dao;

public interface DAOFactory {
	public enum EntityType {
		USER, USERSTAT, GAME, MESSAGE, ACCOUNT_OPERATION;
	}
	
	EntityDAO getDAO(EntityType type);
}
