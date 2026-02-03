package com.peluqueria.front_end.application.ports.out;

import com.peluqueria.front_end.domain.User;

/**
 * The Interface LoadUser.
 */
public interface UserPortOut {
	
	/**
	 * Login And Register.
	 *
	 * @param user the user
	 * @param sessionId the session id
	 * @return the user
	 */
	String login(User user, String sessionId);
	
	/**
	 * Register.
	 *
	 * @param user the user
	 * @param sessionId the session id
	 * @return the string
	 */
	String register(User user, String sessionId);
	
	/**
	 * deleteUserToken.
	 *
	 * @param sessionId the sessionId
	 */
	void deleteUserToken(String sessionId);
	
	/**
	 * forgotPassword.
	 *
	 * @param email the email
	 * @return the string
	 */
	String forgotPassword(String email);
	
	/**
	 * resetPassword.
	 *
	 * @param newPass the newPass
	 * @param token the token
	 * @return the integer
	 */
	Integer resetPassword(String newPass, String token);
}
