package com.peluqueria.api.application.usercases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.peluqueria.api.application.ports.in.UserPortIn;
import com.peluqueria.api.application.ports.out.UserPortOut;
import com.peluqueria.api.domain.User;
import com.peluqueria.api.domain.dto.UserSimpleDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class UserUsecase.
 */
@Service
@RequiredArgsConstructor
public class UserUsecase implements UserPortIn {
	
	/** The user port out. */
	private final UserPortOut userPortOut;

	/**
	 * Load.
	 *
	 * @param id the id
	 * @return the user
	 */
	@Override
	public User load(Long id) {
		
		return userPortOut.load(id);
	}

	/**
	 * Update.
	 *
	 * @param user the user
	 * @return the user
	 */
	@Override
	public User update(User user) {
		
		return userPortOut.update(user);
	}

	/**
	 * Disable user.
	 *
	 * @param userId the user id
	 */
	@Override
	public void disableUser(Long userId) {
		
		userPortOut.disableUser(userId);
	}
	
	/**
	 * Find all for selection.
	 *
	 * @return the list
	 */
	@Override
    public List<UserSimpleDto> findAllForSelection() {
		
        return userPortOut.findAllForSelection();
    }
}
