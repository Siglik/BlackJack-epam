package org.qqq175.blackjack.logic.player;

import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_CARDHOLDER;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_CARD_NUMBER;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_SUM;
import static org.qqq175.blackjack.StringConstant.PARAMETER_PAYMENT_TYPE;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import org.qqq175.blackjack.exception.DAOException;
import org.qqq175.blackjack.persistence.connection.ConnectionPool;
import org.qqq175.blackjack.persistence.connection.ConnectionWrapper;
import org.qqq175.blackjack.persistence.dao.AccountOperationDAO;
import org.qqq175.blackjack.persistence.dao.DAOFactory;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.dao.util.Settings;
import org.qqq175.blackjack.persistence.entity.AccountOperation;
import org.qqq175.blackjack.persistence.entity.AccountOperation.Type;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.persistence.entity.id.UserId;

public class AccountOperationLogic {
	private static final String CARD_NUM = "card number: ";
	private static final String ASTERISK = "*";
	private static final String CARDHOLDER = "cardholder: ";
	private static final String REGEXP_DELIMETER = "\\s";
	private static final String REGEXP_MULTISPASE = "\\s{2,}";
	private static final String NOT_DIGIT = "\\D";

	public enum Result {
		OK("Ok"), NOT_ENOUGH_MONEY("This email is already used!"), BALANCE_ERROR("Unkown account balance error."), WRONG_DATA("Invalid data.");
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
					userDAO.updateBalance(userId, change, conn);
					AccountOperationDAO aoDAO = daoFactory.getAccountOperationDAO();
					aoDAO.create(oper, conn);
					conn.commit();
					result = Result.OK;
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
		/*
		 * sum=12.1 &number=5444444444445555 &exp-month=12 &expr-year=13
		 * &swift=153 &cardholder=maxim+mihalkov &operation=payment
		 */

		return true;
	}
}
