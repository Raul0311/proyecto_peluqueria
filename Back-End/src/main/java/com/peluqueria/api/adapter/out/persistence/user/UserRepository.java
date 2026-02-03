package com.peluqueria.api.adapter.out.persistence.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

/**
 * The Interface UserRepository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	/**
	 * Validate the userToken.
	 *
	 * @param userToken the user token
	 * @return if the userToken is validate
	 */
	@Query(value="SELECT user_id FROM user_tokens WHERE user_token = ?1", nativeQuery = true)
	Long validateUserTokenByUserToken(String userToken);
	
	/**
	 * Disable user.
	 *
	 * @param userId the user id
	 * @return if the user is disabled
	 */
	@Modifying
	@Transactional
	@Query(value="UPDATE users u SET u.enabled = false WHERE u.id = ?1", nativeQuery = true)
	Integer disableUser(Long userId);
	
	/**
	 * Find all users with roles.
	 *
	 * @return the users with roles
	 */
	@Query("SELECT u FROM UserEntity u JOIN FETCH u.userRoles ur JOIN FETCH ur.role")
    List<UserEntity> findAllWithRoles();
	
	/**
	 * Find user by id with roles.
	 *
	 * @param userId the user id
	 * @return the user with roles
	 */
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.userRoles ur JOIN FETCH ur.role WHERE u.id = :userId")
    Optional<UserEntity> findByIdWithRoles(@Param("userId") Long userId);
}