package com.example.demo.application.ports.out;

import java.util.List;

import com.example.demo.domain.dto.RoleDto;
import com.example.demo.domain.dto.UserWithRolesDto;

/**
 * The Interface RolePortOut.
 */
public interface RolesPortOut {	
	
	/**
	 * LoadAllUsersWithRoles.
	 *
	 * @return the users with roles
	 */
	List<UserWithRolesDto> loadAllUsersWithRoles();
	
	/**
	 * LoadAllRoles.
	 *
	 * @return the roles
	 */
    List<RoleDto> loadAllRoles();
    
    /**
     * LoadUserWithRoles.
     *
     * @param userId the user id
     * @return the user with roles
     */
    UserWithRolesDto loadUserWithRoles(Long userId);
    
	/**
	 * RemoveAllRolesFromUser.
	 *
	 * @param userId the user id
	 * @param roleNames the role names
	 */
	void removeSpecificRolesFromUser(Long userId, List<String> roleNames);
	
	/**
	 * AssignRolesToUser.
	 *
	 * @param userId the user id
	 * @param roleNames the role names
	 */
    void assignRolesToUser(Long userId, List<String> roleNames);
    
    /**
	 * FindRoleIdByName.
	 *
	 * @param roleName the role name
	 * @return the role id
	 */
    Long findRoleIdByName(String roleName);
    
    /**
     * Persists a new role entity.
     *
     * @param finalRoleName the final role name
     * @param description the description
     */
    void saveRole(String finalRoleName, String description);
    
    /**
     * Elimina un rol por su nombre.
     * @param roleName Nombre del rol
     */
    void removeRole(String roleName);
    
    /**
     * Find roles by user id.
     *
     * @param userId the user id
     * @return the list
     */
    List<String> findRolesByUserId(Long userId);
}
