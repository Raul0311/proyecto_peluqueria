package com.example.demo.application.ports.out;

/**
 * The Interface EmailPortOut.
 */
public interface EmailPortOut {
	
	/**
	 * SendResetPasswordEmail.
	 *
	 * @param email the email
	 * @param token the token
	 */
	void sendResetPasswordEmail(String email, String token);
}
