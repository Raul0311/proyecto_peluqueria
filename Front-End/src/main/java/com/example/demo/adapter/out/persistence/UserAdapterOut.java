package com.example.demo.adapter.out.persistence;

import org.springframework.stereotype.Service;

import com.example.demo.application.ports.out.UserPortOut;
import com.example.demo.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * The Class UserAdapterOut.
 */
@Service
@RequiredArgsConstructor
public class UserAdapterOut implements UserPortOut {
	
	/**
	 * The Class DatabaseOperationException.
	 */
	@SuppressWarnings("serial")
	public class DatabaseOperationException extends RuntimeException {
	    
    	/**
    	 * Instantiates a new database operation exception.
    	 *
    	 * @param message the message
    	 * @param cause the cause
    	 */
    	public DatabaseOperationException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}

	/**
	 * The Class UserSessionException.
	 */
	@SuppressWarnings("serial")
	public class UserSessionException extends RuntimeException {	    
	    
    	/**
    	 * Instantiates a new user session exception.
    	 *
    	 * @param message the message
    	 * @param cause the cause
    	 */
    	public UserSessionException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}

	/** The user repository. */
	private final UserRepository userRepository;

	/**
	 * Load.
	 *
	 * @param user the user
	 * @param sessionId the session id
	 * @return the user
	 */
	@SuppressWarnings("unused")
	@Override
	public String login(User user, String sessionId) {
		String userData = null;
	    
	    try {
        	userData = userRepository.login(user.getUsername(), user.getPassw(), sessionId);
        	
        	if (userData == null) {
		        return null;
		    }
        	
        	return userData;
	    } catch (org.springframework.dao.DataAccessException e) {
	    	throw new DatabaseOperationException("Error al procesar la autenticación en la base de datos", e);
	    }
	}
	
	/**
	 * Register.
	 *
	 * @param user the user
	 * @param sessionId the session id
	 * @return the string
	 */
	@Override
	public String register(User user, String sessionId) {
		String userData = null;
		
		try {
			userData = userRepository.register(user, sessionId);
	    	
	    	if (userData == null) {
		        return null;
		    }
	    	
	    	return userData;
		} catch (org.springframework.dao.DataAccessException e) {
	    	throw new DatabaseOperationException("Error al procesar la autenticación en la base de datos", e);
	    }
	}
	
	/**
	 * deleteUserToken.
	 *
	 * @param sessionId the session id
	 */
	@Override
	@Transactional
	public void deleteUserToken(String sessionId) {
		try {
			userRepository.deleteUserToken(sessionId);
		} catch (org.springframework.dao.DataAccessException e) {
			throw new UserSessionException("No se pudo eliminar el token de sesión", e);
		}
	}

	/**
	 * forgotPassword.
	 *
	 * @param email the email
	 * @return the string
	 */
	@Override
	public String forgotPassword(String email) {
		try {
			return userRepository.forgotPassword(email);
		} catch (org.springframework.dao.DataAccessException e) {
			throw new DatabaseOperationException("Error al solicitar recuperación de contraseña para: " + email, e);
		}
	}

	/**
	 * resetPassword.
	 *
	 * @param newPass the newPass
	 * @param token the token
	 * @return the integer
	 */
	@Override
	public Integer resetPassword(String newPass, String token) {
		try {
			return userRepository.resetPassword(newPass, token);
		} catch (org.springframework.dao.DataAccessException e) {
			throw new DatabaseOperationException("Error al resetear la contraseña con el token proporcionado", e);
		}
	}
}