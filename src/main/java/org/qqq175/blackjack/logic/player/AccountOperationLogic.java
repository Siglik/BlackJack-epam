package org.qqq175.blackjack.logic.player;

import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_CARDHOLDER;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_CARD_NUMBER;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_CVV;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_EXPR_MONTH;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_EXPR_YEAR;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_SUM;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_TYPE;
import static org.qqq175.blackjack.StringConstant.PATTERN_CARD;
import static org.qqq175.blackjack.StringConstant.PATTERN_CARDHOLDER;
import static org.qqq175.blackjack.StringConstant.PATTERN_CARD_CVV;
import static org.qqq175.blackjack.StringConstant.PATTERN_MONTH;
import static org.qqq175.blackjack.StringConstant.PATTERN_PAYMENT_OPERATION;
import static org.qqq175.blackjack.StringConstant.PATTERN_PAYMENT_SUM;
import static org.qqq175.blackjack.StringConstant.PATTERN_YEAR_2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.persistence.connection.ConnectionPool;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.dao.AccountOperationDAO;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.AccountOperation.Type;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.AccountOperationId;
import org.qqq175.blackjack.persistence.entity.id.UserId;

/**
 * contains methods to perform account operations
 * 
 * @author qqq175
 *
 */
public class AccountOperationLogic {
	private static final String UNABLE_ACCOUNT_OPERATION = "Unable to perform account operation.";

	private static final String UNABLE_ROLLBACK = "Unable to rollback.";

	private static final String UNABLE_AUTOCOMMIT = "Unable to change autocommit state.";

	private static final String UNABLE_TOTAL_OPERATIONS = "Unable to calc user's total operations";

	private static final String UNABLE_OPERATIONS_LIST = "Unable to get user operations list.";

	private static final String UNABLE_OPERATIONS_COUNT = "Unable to get operations count.";

	private static Logger log = LogManager.getLogger(AccountOperationLogic.class);

	private static final String CARD_NUM = "card number: ";
	private static final String ASTERISK = "*";
	private static final String CARDHOLDER = "cardholder: ";
	private static final String REGEXP_DELIMETER = "\\s";
	private static final String REGEXP_MULTISPASE = "\\s{2,}";
	private static final String NOT_DIGIT = "\\D";

	/**
	 * Possible account operation results
	 * 
	 * @author qqq175
	 *
	 */
	public enum Result {
		OK("Ok"), NOT_ENOUGH_MONEY("Not enough money!"), BALANCE_ERROR("Unkown account balance error."), WRONG_DATA("Invalid data.");
		private String message;

		private Result(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	/**
	 * Make payment or withdrawal. Params must contain PARAMETER_PAYMENT_TYPE,
	 * PARAMETER_PAYMENT_SUM, PARAMETER_PAYMENT_CARD_NUMBER,
	 * PARAMETER_PAYMENT_CARDHOLDER.
	 * 
	 * @param params
	 *            - request params map
	 * @param userId
	 * @return
	 */
	public Result doPayment(Map<String, String[]> params, UserId userId) {
		Result result = null;
		/* validate end extract params */
		if (isValid(params)) {
			AccountOperation oper = new AccountOperation();
			oper.setType(Type.valueOf(params.get(PARAMETER_PAYMENT_TYPE)[0].toUpperCase()));
			oper.setAmmount(new BigDecimal(params.get(PARAMETER_PAYMENT_SUM)[0]));
			oper.setUserId(userId);
			String card = params.get(PARAMETER_PAYMENT_CARD_NUMBER)[0];
			String cardHolder = params.get(PARAMETER_PAYMENT_CARDHOLDER)[0];
			oper.setComment(this.generatePaymentComment(card, cardHolder));

			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			/* chack if have enough money */
			if (oper.getType() == AccountOperation.Type.WITHDRAWAL) {
				try {
					User user = daoFactory.getUserDAO().findEntityById(userId);
					if (user.getAccountBalance().compareTo(oper.getAmmount()) < 0) {
						result = Result.NOT_ENOUGH_MONEY;
					}
				} catch (DAOException e2) {
					result = Result.BALANCE_ERROR;
					log.error(UNABLE_ACCOUNT_OPERATION, e2);
				}
			}
			/* if not error result - resume */
			if (result == null) {
				ConnectionWrapper conn = ConnectionPool.getInstance().retrieveConnection();
				/* do transaction */
				try {
					conn.setAutoCommit(false);
					UserDAO userDAO = daoFactory.getUserDAO();
					BigDecimal change;
					if (oper.getType() == AccountOperation.Type.WITHDRAWAL) {
						change = oper.getAmmount().negate();
					} else {
						change = oper.getAmmount();
					}
					boolean balUpdated = userDAO.updateBalance(userId, change, conn);

					AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
					AccountOperationId aoId = aoDAO.create(oper, conn);

					// check result and commit or roll back
					if (aoId != null && balUpdated) {
						conn.commit();
						result = Result.OK;
					} else {
						conn.rollback();
						result = Result.BALANCE_ERROR;
						log.error(UNABLE_ACCOUNT_OPERATION + oper.getComment() + " " + oper.getType() + " " + oper.getAmmount());
					}
				} catch (SQLException | DAOException e) {
					log.error(UNABLE_AUTOCOMMIT, e);
					if (conn != null) {
						try {
							result = Result.BALANCE_ERROR;
							conn.rollback();
						} catch (SQLException e1) {
							log.error(UNABLE_ROLLBACK, e1);
						}
					}
					e.printStackTrace();
				} finally {
					if (conn != null) {
						try {
							conn.setAutoCommit(true);
						} catch (SQLException e) {
							log.error(UNABLE_AUTOCOMMIT, e);
						}
						conn.close();
					}
				}
			}
		} else {
			result = Result.WRONG_DATA;
		}

		if (result == null) {
			result = Result.BALANCE_ERROR;
		}
		return result;
	};

	public Result doGamePayment(BigDecimal win, BigDecimal back, BigDecimal loss, UserId userId, AccountOperation.Type type, String comment) {

		Result result = null;
		BigDecimal change = win.subtract(loss).abs();
		AccountOperation oper = null;
		if (type != null) {
			oper = new AccountOperation();
			oper.setType(type);
			oper.setAmmount(change);
			oper.setUserId(userId);
			oper.setComment(comment);
		}
		/*
		 * check if enough money on main and locked balance.
		 */
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		if (!back.equals(BigDecimal.ZERO) || !loss.equals(BigDecimal.ZERO)) {
			try {
				User user = daoFactory.getUserDAO().findEntityById(userId);
				if (user.getLockedBalance().compareTo(back.add(loss)) < 0) {
					result = Result.NOT_ENOUGH_MONEY;
				}
			} catch (DAOException e2) {
				result = Result.BALANCE_ERROR;
				log.error(UNABLE_ACCOUNT_OPERATION, e2);
			}
		}
		/* if not error result - resume */
		if (result == null) {
			ConnectionWrapper conn = ConnectionPool.getInstance().retrieveConnection();
			/* do transaction */
			try {
				conn.setAutoCommit(false);
				boolean balUpdated, balUnlocked, balDecreased, operOk;
				UserDAO userDAO = daoFactory.getUserDAO();
				if (!win.equals(BigDecimal.ZERO)) {
					balUpdated = userDAO.updateBalance(userId, win, conn);
				} else {
					balUpdated = true;
				}
				AccountOperationId aoId = null;
				if (oper != null) {
					AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
					operOk = aoDAO.create(oper, conn) != null;
				} else {
					operOk = true;
				}
				if (!back.equals(BigDecimal.ZERO)) {
					balUnlocked = userDAO.unlockBalance(userId, back, conn);
				} else {
					balUnlocked = true;
				}
				if (!loss.equals(BigDecimal.ZERO)) {
					balDecreased = userDAO.decreaceLockedBalance(userId, loss, conn);
				} else {
					balDecreased = true;
				}

				// check result and commit or roll back
				if (operOk && balUpdated && balUnlocked & balDecreased) {
					conn.commit();
					result = Result.OK;
				} else {
					conn.rollback();
					result = Result.BALANCE_ERROR;
					log.error(UNABLE_ACCOUNT_OPERATION + comment);
				}

			} catch (SQLException | DAOException e) {
				if (conn != null) {
					log.error(UNABLE_AUTOCOMMIT, e);
					try {
						result = Result.BALANCE_ERROR;
						conn.rollback();
					} catch (SQLException e1) {
						log.error(UNABLE_ROLLBACK, e1);
					}
				}
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.setAutoCommit(true);
					} catch (SQLException e) {
						log.error(UNABLE_AUTOCOMMIT, e);
					}
					conn.close();
				}
			}
		}

		if (result == null) {
			result = Result.BALANCE_ERROR;
		}
		return result;
	};

	/**
	 * Calc total user's operations of each Account operation type
	 * 
	 * @param id
	 *            - user id
	 * @return
	 * @throws LogicException
	 */
	public Map<String, BigDecimal> calcTotals(UserId id) throws LogicException {
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
		Map<String, BigDecimal> result = new HashMap<>();

		try {
			for (AccountOperation.Type type : AccountOperation.Type.values()) {
				BigDecimal total = aoDAO.calcTotal(type, id);
				if (total != null) {
					result.put(type.name().toLowerCase(), total);
				} else {
					result.put(type.name().toLowerCase(), new BigDecimal(0));
				}
			}
		} catch (DAOException e) {
			throw new LogicException(UNABLE_TOTAL_OPERATIONS, e);
		}

		return result;
	}

	/**
	 * Calc total server operations of each Account operation type
	 * 
	 * @return
	 * @throws LogicException
	 */
	public Map<String, BigDecimal> calcTotals() throws LogicException {
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
		Map<String, BigDecimal> result = new HashMap<>();

		try {
			for (AccountOperation.Type type : AccountOperation.Type.values()) {
				BigDecimal total = aoDAO.calcTotal(type);
				if (total != null) {
					result.put(type.name().toLowerCase(), total);
				} else {
					result.put(type.name().toLowerCase(), new BigDecimal(0));
				}
			}
		} catch (DAOException e) {
			throw new LogicException(UNABLE_TOTAL_OPERATIONS, e);
		}

		return result;
	}

	/**
	 * return user operations with pagination
	 * 
	 * @param userId
	 * @param page
	 *            - page to display
	 * @param operationsPerPage
	 *            - number of entries per page
	 * @return
	 * @throws LogicException
	 */
	public List<AccountOperation> findUserOperations(UserId userId, int page, int operationsPerPage) throws LogicException {
		List<AccountOperation> result;
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
		int from = (page - 1) * operationsPerPage;
		try {
			result = aoDAO.findUserOperationsPaginated(userId, from, operationsPerPage);
		} catch (DAOException e) {
			throw new LogicException(UNABLE_OPERATIONS_LIST, e);
		}

		return result;
	}

	/**
	 * count user operations count
	 * 
	 * @param id
	 * @return
	 * @throws LogicException
	 */
	public Long countOpers(UserId id) throws LogicException {
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();

		try {
			return aoDAO.countOperations(id);
		} catch (DAOException e) {
			throw new LogicException(UNABLE_OPERATIONS_COUNT, e);
		}
	}

	private String generatePaymentComment(String card, String cardHolder) {
		StringBuilder sb = new StringBuilder();
		String localCard = card.replaceAll(NOT_DIGIT, "");
		sb.append(CARD_NUM);
		sb.append(localCard.charAt(0));
		for (int i = 0; i < localCard.length() - 5; i++) {
			sb.append(ASTERISK);
		}
		sb.append(localCard.substring(localCard.length() - 4));
		sb.append(", ");
		sb.append(CARDHOLDER);
		String[] name = cardHolder.replace(REGEXP_MULTISPASE, " ").split(REGEXP_DELIMETER);
		if (name.length == 2) {
			sb.append(name[0]).append(" ");
			sb.append(name[1].charAt(0));
		}
		return sb.toString();
	}

	/**
	 * validates request params
	 * 
	 * @param params
	 * @return
	 */
	private boolean isValid(Map<String, String[]> params) {
		return validateParameter(params, PARAMETER_PAYMENT_SUM, PATTERN_PAYMENT_SUM, true)
				&& validateParameter(params, PARAMETER_PAYMENT_CARD_NUMBER, PATTERN_CARD, true)
				&& validateParameter(params, PARAMETER_PAYMENT_EXPR_MONTH, PATTERN_MONTH, true)
				&& validateParameter(params, PARAMETER_PAYMENT_EXPR_YEAR, PATTERN_YEAR_2, true)
				&& validateParameter(params, PARAMETER_PAYMENT_CVV, PATTERN_CARD_CVV, true)
				&& validateParameter(params, PARAMETER_PAYMENT_CARDHOLDER, PATTERN_CARDHOLDER, true)
				&& validateParameter(params, PARAMETER_PAYMENT_TYPE, PATTERN_PAYMENT_OPERATION, true);
	}

	/**
	 * validate parameter by its pattern
	 * 
	 * @param params
	 * @param parameter
	 * @param pattern
	 * @param isRequired
	 * @return
	 */
	private boolean validateParameter(Map<String, String[]> params, String parameter, String pattern, boolean isRequired) {
		boolean isValid = !isRequired;
		if (params.containsKey(parameter)) {
			String[] values = params.get(parameter);
			if (values.length == 1) {
				if (!values[0].isEmpty()) {
					Pattern ptn = Pattern.compile(pattern);
					Matcher matcher = ptn.matcher(values[0].toLowerCase());
					isValid = matcher.matches();
				}
			}
		}

		if (!isValid) {
			System.out.print(parameter + " is not valid");
		}
		return isValid;
	}
}
