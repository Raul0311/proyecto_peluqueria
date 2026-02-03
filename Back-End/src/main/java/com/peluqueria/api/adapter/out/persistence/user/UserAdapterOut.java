package com.peluqueria.api.adapter.out.persistence.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.peluqueria.api.adapter.out.persistence.roles.RoleEntity;
import com.peluqueria.api.adapter.out.persistence.roles.RoleRepository;
import com.peluqueria.api.adapter.out.persistence.roles.UserRolesEntity;
import com.peluqueria.api.application.ports.out.UserPortOut;
import com.peluqueria.api.domain.User;
import com.peluqueria.api.domain.dto.UserSimpleDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class UserAdapterOut.
 */
@Service
@RequiredArgsConstructor
public class UserAdapterOut implements UserPortOut {
	
	/**
	 * The Class UserNotFoundException.
	 */
	@SuppressWarnings("serial")
	public class UserNotFoundException extends RuntimeException {
	    
    	/**
    	 * Instantiates a new user not found exception.
    	 *
    	 * @param message the message
    	 */
    	public UserNotFoundException(String message) {
	        super(message);
	    }
	}
	
	/**
	 * The Class UserDisableException.
	 */
	@SuppressWarnings("serial")
	public class UserDisableException extends RuntimeException {
	    
    	/**
    	 * Instantiates a new user disable exception.
    	 *
    	 * @param message the message
    	 */
    	public UserDisableException(String message) {
	        super(message);
	    }
	}
	
	/**
	 * The Class UserUpdateException.
	 */
	@SuppressWarnings("serial")
	public class UserUpdateException extends RuntimeException {
	    
    	/**
    	 * Instantiates a new user update exception.
    	 *
    	 * @param message the message
    	 */
    	public UserUpdateException(String message) {
	        super(message);
	    }
	}

	/** The user repository. */
	private final UserRepository userRepository;
	
	/** The role repository. */
	private final RoleRepository roleRepository;
	
	/** The user mapper. */
	private final UserMapper userMapper;

    /**
     * Validate user.
     *
     * @param userToken the user token
     * @return the long
     */
    @Override
    public Long validateUser(String userToken) {
        return userRepository.validateUserTokenByUserToken(userToken);
    }

    /**
     * Load.
     *
     * @param userId the user id
     * @return the user
     */
    @Override
    public User load(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));
        userEntity.setPassw(null);

        return userMapper.toDomain(userEntity);
    }

	/**
	 * Update.
	 *
	 * @param user the user
	 * @return the user
	 */
	@Override
	@Transactional
	public User update(User user) {
		UserEntity userEntity = userRepository.findById(user.getId())
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		
		userMapper.updateEntityFromDomain(user, userEntity);

	    return userMapper.toDomain(userEntity);
	}

	/**
	 * Disable user.
	 *
	 * @param userId the user id
	 */
	@Override
	public void disableUser(Long userId) {
		Integer updated = userRepository.disableUser(userId);
		if (updated == 0) throw new UserDisableException("No se ha podido eliminar la cuenta");
	}
	
	/**
	 * Update user roles.
	 *
	 * @param userId the user id
	 * @param roles the roles
	 */
	@Override
	@Transactional
	public void updateUserRoles(Long userId, List<String> roles) {

	    UserEntity user = userRepository.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

	    // Borrar relaciones actuales
	    user.setUserRoles(new ArrayList<>());

	    // Buscar roles existentes
	    List<RoleEntity> roleEntities = roleRepository.findByRoleNameIn(roles);

	    if (roleEntities.size() != roles.size()) {
	        throw new IllegalArgumentException("Uno o más roles no existen");
	    }

	    // Crear relaciones nuevas
	    List<UserRolesEntity> relations = roleEntities.stream()
	            .map(role -> new UserRolesEntity(user, role))
	            .toList();

	    user.getUserRoles().addAll(relations);

	    userRepository.save(user);
	}

	@Override
	public List<UserSimpleDto> findAllForSelection() {
		return userRepository.findAll().stream()
                .map(user -> new UserSimpleDto(
                        user.getId(), 
                        user.getUsername(), 
                        user.getEmail()
                ))
                .toList();
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return userRepository.findById(id);
	}
}