package com.example.demo.application.ports.in;

import java.util.List;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserSimpleDto;

/**
 * The Interface UserPortIn.
 */
public interface UserPortIn {
	
	/**
	 * Load.
	 *
	 * @param id the id
	 * @return the user
	 */
	User load(Long id);
	
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
	 * Find all for selection.
	 *
	 * @return the list
	 */
	public List<UserSimpleDto> findAllForSelection();
}
