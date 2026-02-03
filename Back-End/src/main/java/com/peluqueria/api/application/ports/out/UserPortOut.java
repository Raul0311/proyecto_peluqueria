package com.peluqueria.api.application.ports.out;

import java.util.List;
import java.util.Optional;

import com.peluqueria.api.adapter.out.persistence.user.UserEntity;
import com.peluqueria.api.domain.User;
import com.peluqueria.api.domain.dto.UserSimpleDto;

/**
 * The Interface UserPortOut.
 */
public interface UserPortOut {
	
	/**
	 * Load.
	 *
	 * @param userId the user id
	 * @return the user
	 */
	User load(Long userId);
	
	/**
	 * ValidateUser.
	 *
	 * @param userToken the user token
	 * @return the long
	 */
	Long validateUser(String userToken);
	
	/**
	 * Update.
	 *
	 * @param user the user
	 * @return the updated user
	 */
	User update(User user);
	
	/**
	 * DisableUser.
	 *
	 * @param userId the user id
	 * @return the eliminated user
	 */
	void disableUser(Long userId);
	
	/**
	 * Update user roles.
	 *
	 * @param userId the user id
	 * @param roles the roles
	 */
	void updateUserRoles(Long userId, List<String> roles);
	
	/**
	 * Find all for selection.
	 *
	 * @return the list
	 */
	public List<UserSimpleDto> findAllForSelection();
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the user entity
	 */
	public Optional<UserEntity> findById(Long id);
}
