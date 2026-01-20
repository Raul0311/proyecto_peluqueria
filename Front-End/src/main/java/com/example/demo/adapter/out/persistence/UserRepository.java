package com.example.demo.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	/**
	 * Login and register.
	 *
	 * @param user the user
	 * @param sessionId the session id
	 * @param register the register
	 * @param login the login
	 * @return the string
	 */
	@Query(value = "CALL register("
            + ":#{#user.username}, :#{#user.passw}, :#{#user.name}, :#{#user.lastname1}, "
            + ":#{#user.lastname2}, :#{#user.city}, :#{#user.country}, :#{#user.street}, "
            + ":#{#user.numberAddress}, :#{#user.apartment}, :#{#user.zipCode}, :#{#user.phone}, "
            + ":#{#user.email}, :sessionId)", nativeQuery = true)
    String register(@Param("user") User user, @Param("sessionId") String sessionId);
	
	/**
	 * Find by user.
	 *
	 * @param username       the username
	 * @param passw          the password
	 * @param sessionId      the current session identifier
	 * @return               the JSON containing the user id, token and roles
	 */
	@Query(value="CALL login(?1, ?2, ?3)", nativeQuery = true)
	String login(String username, String passw, String sessionId);
	
	/**
	 * Delete userToken.
	 *
	 * @param sessionId the session id
	 */
	@Query(value="DELETE FROM user_tokens AS u WHERE u.session_id = ?1", nativeQuery = true)
	@Modifying
	void deleteUserToken(String sessionId);
	
	/**
	 * Send email.
	 *
	 * @param email the email
	 * @return the string
	 */
	@Query(value="CALL forgotPassword(?1)", nativeQuery = true)
	String forgotPassword(String email);
	
	/**
	 * Reset password.
	 *
	 * @param newPass the new password
	 * @param token the token
	 * @return the integer
	 */
	@Query(value="CALL resetPassword(?1, ?2)", nativeQuery = true)
	Integer resetPassword(String newPass, String token);
}