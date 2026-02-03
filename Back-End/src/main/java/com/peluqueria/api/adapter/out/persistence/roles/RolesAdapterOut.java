package com.peluqueria.api.adapter.out.persistence.roles;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peluqueria.api.adapter.out.persistence.user.UserEntity;
import com.peluqueria.api.adapter.out.persistence.user.UserRepository;
import com.peluqueria.api.application.ports.out.RolesPortOut;
import com.peluqueria.api.domain.dto.RoleDto;
import com.peluqueria.api.domain.dto.UserWithRolesDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class RolesAdapterOut.
 */
@Service
@RequiredArgsConstructor
public class RolesAdapterOut implements RolesPortOut {
	
    /** The user repository. */
    // Asegúrate de que los constructores inyecten estos tres
	private final UserRepository userRepository;
    
    /** The role repository. */
    private final RoleRepository roleRepository;
    
    /** The user roles repository. */
    private final UserRolesRepository userRolesRepository;
    
    /** The role mapper. */
    private final RoleMapper roleMapper;
    
    /**
     * Map user entity to dto.
     *
     * @param user the user
     * @return the user with roles dto
     */
    private UserWithRolesDto mapUserEntityToDto(UserEntity user) {
        List<RoleDto> roleDtos = user.getRoles().stream()
                .map(roleMapper::toDto)
                .toList();
        
        UserWithRolesDto dto = new UserWithRolesDto(user.getId(), user.getUsername(), null); 
        dto.setRoles(roleDtos);
        return dto;
    }

    /**
     * Load all users with roles.
     *
     * @return the list
     */
    @Override
    public List<UserWithRolesDto> loadAllUsersWithRoles() {
    	return userRepository.findAllWithRoles().stream()
                .map(this::mapUserEntityToDto)
                .toList();
    }
    
    /**
     * Load user with roles.
     *
     * @param userId the user id
     * @return the user with roles dto
     */
    @Override
    public UserWithRolesDto loadUserWithRoles(Long userId) {
    	return userRepository.findByIdWithRoles(userId)
                .map(this::mapUserEntityToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Load all roles.
     *
     * @return the list
     */
    @Override
    public List<RoleDto> loadAllRoles() {
    	return roleMapper.toDtoList(roleRepository.findAll());
    }

    /**
     * Removes the specific roles from user.
     *
     * @param userId the user id
     * @param roleNames the role names
     */
    @Override
    @Transactional
    public void removeSpecificRolesFromUser(Long userId, List<String> roleNames) {
    	// Obtenemos los IDs de los roles a partir de sus nombres
    	List<Long> roleIds = roleRepository.findIdsByRoleNames(roleNames);
        
        // Usamos el nuevo método en el repositorio
    	userRolesRepository.deleteByUserIdAndRoleIds(userId, roleIds);
    }

    /**
     * Assign roles to user.
     *
     * @param userId the user id
     * @param roleNames the role names
     */
    @Override
    @Transactional
    public void assignRolesToUser(Long userId, List<String> roleNames) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        roleNames.forEach(name -> 
            roleRepository.findByName(name).ifPresent(role -> {
                UserRolesEntity newRole = new UserRolesEntity(user, role);
                userRolesRepository.save(newRole);
            })
        );
    }

    /**
     * Find role id by name.
     *
     * @param roleName the role name
     * @return the long
     */
    @Override
    public Long findRoleIdByName(String roleName) {
        // Usa el nuevo método en RoleRepository
        return roleRepository.findByName(roleName).map(RoleEntity::getId).orElse(null);
    }
    
    /**
     * Save role.
     *
     * @param roleName the role name
     * @param description the description
     */
    @Override
    @Transactional
    public void saveRole(String roleName, String description) {
        RoleEntity newRole = new RoleEntity();
        newRole.setRoleName(roleName);
        newRole.setDescription(description);
        
        roleRepository.save(newRole);
    }
    
    /**
     * Removes the role.
     *
     * @param roleName the role name
     */
    @Override
    @Transactional
    public void removeRole(String roleName) {
        // Esto asume que RoleRepository tiene un método para eliminar por nombre
        // Opcional: Podrías necesitar primero cargar la entidad y luego borrarla
        roleRepository.deleteByRoleName(roleName); 
    }
    
    /**
     * Find roles by user id.
     *
     * @param userId the user id
     * @return the list
     */
    @Override
    public List<String> findRolesByUserId(Long userId) {
        // Si usas Spring Data JPA en un RolesRepository:
        return roleRepository.findAllByUserId(userId); 
    }
}