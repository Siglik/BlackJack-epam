package org.qqq175.blackjack.logic.main;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.qqq175.blackjack.exception.LogicException;
import org.qqq175.blackjack.persistence.dao.util.Settings;

/**
 * Contains methods to calculate passowrd hash, perform salt
 * 
 * @author qqq175
 *
 */
public class SecurityLogic {

	/**
	 * calc password sha-256 hash
	 * 
	 * @param input
	 * @return
	 * @throws LogicException
	 */
	public String calcSHA(String input) throws LogicException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(input.getBytes("UTF-8"));
			byte[] digest = md.digest();

			return String.format("%064x", new java.math.BigInteger(1, digest));

		} catch (UnsupportedEncodingException e) {
			throw new LogicException(e);
		} catch (NoSuchAlgorithmException e1) {
			throw new LogicException(e1);
		}
	}

	/**
	 * salt password (add salt)
	 * 
	 * @param password
	 * @return
	 */
	public String saltPassword(String password) {
		return password + Settings.getInstance().getSalt();
	}

	/**
	 * addition salt unique for each email
	 * 
	 * @param password
	 * @return
	 */
	public String pepperPassword(String password, String email) {
		return password + email.toUpperCase();
	}
}
