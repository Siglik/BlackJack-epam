/**
 * 
 */
package org.qqq175.blackjack.logic;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import org.qqq175.blackjack.exception.EntityNotFoundException;
import org.qqq175.blackjack.persistence.dao.UserDAO;
import org.qqq175.blackjack.persistence.entity.User;
import org.qqq175.blackjack.service.SessionRequestContent;
import org.qqq175.blackjack.util.Settings;

/**
 * @author qqq175
 *
 */
public class LoginLogic {
	
	/**
	 * calc password sha-256 hash
	 * @param input
	 * @return
	 */
	public String calcSHA(String input){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(input.getBytes("UTF-8"));
			byte[] digest = md.digest();
			
			return String.format("%064x", new java.math.BigInteger(1, digest));
			
		} catch (UnsupportedEncodingException e) {
			//TODO
		}  catch (NoSuchAlgorithmException e1) {
			//TODO
		}
		
		return null;
	}
	
	/**
	 * @param password
	 * @return
	 */
	public String saltPassword(String password){
		return password + Settings.getInstance().getSalt();
	}
	
	/**
	 * @param password
	 * @return
	 */
	public String pepperPassword(String password, String email){
		return password + email.toUpperCase();
	}
	
	
	/**
	 * If user and pass is right return true, else return false
	 * @param login
	 * @param passwordHash
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public boolean loginUser(String email, String password, Connection connection, SessionRequestContent requestContent) throws SQLException{
		String saltedPassword = pepperPassword(saltPassword(password), email);

		UserDAO userDAO = Settings.getInstance().getDaoFactory().getUserDAO();
		User user = null;
		try{
			user = userDAO.findUser(email, saltedPassword);
		} catch (EntityNotFoundException e) {
			//TODO
		}
		
		if(user != null){
			return applyUserToSession(user, requestContent);
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param login
	 * @param passwordHash
	 * @param connection
	 * @param requestContent
	 * @return
	 */
	private boolean applyUserToSession(User user, SessionRequestContent requestContent){
		requestContent.setSessionAttribute("user", user);
		return true;
	}
}
