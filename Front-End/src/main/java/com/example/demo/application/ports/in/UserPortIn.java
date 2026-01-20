package com.example.demo.application.ports.in;

import org.springframework.security.core.Authentication;

import com.example.demo.domain.User;

/**
 * The Interface LoadUser.
 */
public interface UserPortIn {
	
	/**
	 * Load.
	 *
	 * @param auth the auth
	 * @return the URL to load de ModelAndView
	 */
	String load(Authentication auth);
	
	/**
	 * Forgot password.
	 *
	 * @param email the email
	 * @return the string
	 */
	String forgotPassword(String email);
	
	/**
	 * Reset password.
	 *
	 * @param password the password
	 * @param token the token
	 * @return the string
	 */
	String resetPassword(String password, String token);
	
	/**
	 * Register.
	 *
	 * @param user the user
	 * @return the string
	 */
	String register(User user);
}
