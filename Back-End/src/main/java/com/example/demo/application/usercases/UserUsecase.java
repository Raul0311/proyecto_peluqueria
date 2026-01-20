package com.example.demo.application.usercases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.ports.in.UserPortIn;
import com.example.demo.application.ports.out.UserPortOut;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserSimpleDto;

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
