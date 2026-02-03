package com.peluqueria.api.application.ports.in;

import java.util.List;

import com.peluqueria.api.application.rolecases.RoleUpdateCommand;
import com.peluqueria.api.domain.dto.RoleCreationDto;
import com.peluqueria.api.domain.dto.RoleDto;
import com.peluqueria.api.domain.dto.UserWithRolesDto;

/**
 * The Interface LoadRole.
 */
public interface RolesPortIn {
	
	/**
	 * GetAllUsersWithRoles.
	 *
	 * @return the Users with all roles
	 */
	List<UserWithRolesDto> getAllUsersWithRoles();
	
	/**
	 * GetAllRoles.
	 *
	 * @return the roles
	 */
    List<RoleDto> getAllRoles();
    
    /**
     * UpdateUserRoles.
     *
     * @param authenticatedUserId the authenticated user id
     * @param command the command
     */
    void updateUserRoles(Long authenticatedUserId, RoleUpdateCommand command);
    
    /**
     * Creates a new role in the system.
     *
     * @param roleDto the role and description
     */
    void createNewRole(RoleCreationDto roleDto);
    
    /**
     * Elimina un rol del sistema.
     * @param roleName Nombre del rol a eliminar
     */
    void deleteRole(String roleName);
}
