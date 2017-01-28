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

public class AccountOperationLogic {
	private static Logger log = LogManager.getLogger(AccountOperationLogic.class);

	private static final String CARD_NUM = "card number: ";
	private static final String ASTERISK = "*";
	private static final String CARDHOLDER = "cardholder: ";
	private static final String REGEXP_DELIMETER = "\\s";
	private static final String REGEXP_MULTISPASE = "\\s{2,}";
	private static final String NOT_DIGIT = "\\D";

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

	public Result doPayment(Map<String, String[]> params, UserId userId) {
		Result result = null;
		if (isValid(params)) {
			AccountOperation oper = new AccountOperation();
			oper.setType(Type.valueOf(params.get(PARAMETER_PAYMENT_TYPE)[0].toUpperCase()));
			oper.setAmmount(new BigDecimal(params.get(PARAMETER_PAYMENT_SUM)[0]));
			oper.setUserId(userId);
			String card = params.get(PARAMETER_PAYMENT_CARD_NUMBER)[0];
			String cardHolder = params.get(PARAMETER_PAYMENT_CARDHOLDER)[0];
			oper.setComment(this.generatePaymentComment(card, cardHolder));

			DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
			if (oper.getType() == AccountOperation.Type.WITHDRAWAL) {
				try {
					User user = daoFactory.getUserDAO().findEntityById(userId);
					if (user.getAccountBalance().compareTo(oper.getAmmount()) < 0) {
						result = Result.NOT_ENOUGH_MONEY;
					}
				} catch (DAOException e2) {
					result = Result.BALANCE_ERROR;
					e2.printStackTrace();
				}
			}
			if (result == null) {
				ConnectionWrapper conn = ConnectionPool.getInstance().retrieveConnection();
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
					conn.commit();
					if (aoId != null && balUpdated) {
						conn.commit();
						result = Result.OK;
					} else {
						conn.rollback();
						result = Result.BALANCE_ERROR;
						log.error("Unable to make game payment. " + oper.getComment() + " " + oper.getType() + " " + oper.getAmmount());
					}
				} catch (SQLException | DAOException e) {
					if (conn != null) {
						try {
							result = Result.BALANCE_ERROR;
							conn.rollback();
						} catch (SQLException e1) {
							// TODO LOG
							e1.printStackTrace();
						}
					}
					e.printStackTrace();
				} finally {
					if (conn != null) {
						try {
							conn.setAutoCommit(true);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
			oper.setAmmount(win.subtract(loss).abs());
			oper.setUserId(userId);
			oper.setComment(comment);
		}

		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		if (!back.equals(BigDecimal.ZERO) || !loss.equals(BigDecimal.ZERO)) {
			try {
				User user = daoFactory.getUserDAO().findEntityById(userId);
				if (user.getLockedBalance().compareTo(oper.getAmmount()) < 0) {
					result = Result.NOT_ENOUGH_MONEY;
				}
			} catch (DAOException e2) {
				result = Result.BALANCE_ERROR;
				e2.printStackTrace();
			}
		}
		if (result == null) {
			ConnectionWrapper conn = ConnectionPool.getInstance().retrieveConnection();
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
				if (operOk && balUpdated && balUnlocked & balDecreased) {
					conn.commit();
					result = Result.OK;
				} else {
					conn.rollback();
					result = Result.BALANCE_ERROR;
					log.error("Unable to make game payment: " + comment);
				}

			} catch (SQLException | DAOException e) {
				if (conn != null) {
					try {
						result = Result.BALANCE_ERROR;
						conn.rollback();
					} catch (SQLException e1) {
						// TODO LOG
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.setAutoCommit(true);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
			throw new LogicException("Unable to calc user's total operations", e);
		}

		return result;
	}

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
			throw new LogicException("Unable to calc user's total operations", e);
		}

		return result;
	}

	public List<AccountOperation> findUserOperations(UserId userId, int page, int operationsPerPage) throws LogicException {
		List<AccountOperation> result;
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
		int from = (page - 1) * operationsPerPage;
		try {
			result = aoDAO.findUserOperationsLimit(userId, from, operationsPerPage);
		} catch (DAOException e) {
			throw new LogicException("Unable to get users list.", e);
		}

		return result;
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

	private boolean isValid(Map<String, String[]> params) {
		return validateParameter(params, PARAMETER_PAYMENT_SUM, PATTERN_PAYMENT_SUM, true)
				&& validateParameter(params, PARAMETER_PAYMENT_CARD_NUMBER, PATTERN_CARD, true)
				&& validateParameter(params, PARAMETER_PAYMENT_EXPR_MONTH, PATTERN_MONTH, true)
				&& validateParameter(params, PARAMETER_PAYMENT_EXPR_YEAR, PATTERN_YEAR_2, true)
				&& validateParameter(params, PARAMETER_PAYMENT_CVV, PATTERN_CARD_CVV, true)
				&& validateParameter(params, PARAMETER_PAYMENT_CARDHOLDER, PATTERN_CARDHOLDER, true)
				&& validateParameter(params, PARAMETER_PAYMENT_TYPE, PATTERN_PAYMENT_OPERATION, true);
	}

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

	public Long countOpers(UserId id) throws LogicException {
		DAOFactory daoFactory = Settings.getInstance().getDaoFactory();
		AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();

		try {
			return aoDAO.countOperations(id);
		} catch (DAOException e) {
			throw new LogicException("Unable to get operations count.", e);
		}
	}
}
