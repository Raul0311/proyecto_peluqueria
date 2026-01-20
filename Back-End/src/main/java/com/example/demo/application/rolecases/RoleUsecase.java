package com.example.demo.application.rolecases;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.application.ports.in.RolesPortIn;
import com.example.demo.application.ports.out.RolesPortOut;
import com.example.demo.domain.dto.RoleCreationDto;
import com.example.demo.domain.dto.RoleDto;
import com.example.demo.domain.dto.UserWithRolesDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class RoleUsecase.
 */
@Service
@RequiredArgsConstructor
public class RoleUsecase implements RolesPortIn {
	
	/** The roles port out. */
	private final RolesPortOut rolesPortOut;
	
	/** The Constant DEFAULT_ADMIN_ID. */
	private static final Long DEFAULT_ADMIN_ID = 1L;
	
	/** The Constant ADMIN_ROLE. */
	private static final String ADMIN_ROLE = "ROLE_ADMIN";
	
	/** The Constant USER_ROLE. */
	private static final String USER_ROLE = "ROLE_USER";

	/**
	 * Gets the all users with roles.
	 *
	 * @return the all users with roles
	 */
	@Override
	public List<UserWithRolesDto> getAllUsersWithRoles() {
		return rolesPortOut.loadAllUsersWithRoles();
	}

	/**
	 * Gets the all roles.
	 *
	 * @return the all roles
	 */
	@Override
	public List<RoleDto> getAllRoles() {
		return rolesPortOut.loadAllRoles();
	}

	/**
	 * Implementa la validación de seguridad (no quitar ROLE_ADMIN a uno mismo)
	 * y realiza una actualización eficiente (solo INSERT/DELETE de roles cambiados).
	 *
	 * @param authenticatedUserId the authenticated user id
	 * @param command the command
	 */
	@Override
	public void updateUserRoles(Long authenticatedUserId, RoleUpdateCommand command) {
        
        // Cargar roles actuales del usuario que se va a modificar
        UserWithRolesDto targetUserRoles = rolesPortOut.loadUserWithRoles(command.userId());
        
        Set<String> currentRoleNames = targetUserRoles.getRoles().stream()
                .map(RoleDto::name)
                .collect(Collectors.toSet());
        
        Set<String> newRoleNames = Set.copyOf(command.roleNames());

        boolean currentlyAdmin = currentRoleNames.contains(ADMIN_ROLE);
        boolean adminRoleRemoved = currentlyAdmin && !newRoleNames.contains(ADMIN_ROLE);
        
        //  Administrador Predeterminado
        if (command.userId().equals(DEFAULT_ADMIN_ID) && adminRoleRemoved) {
             throw new IllegalStateException("Acceso denegado: No puedes remover el rol '" + ADMIN_ROLE + "' del administrador predeterminado.");
        }
        
        // Restricción: No se puede quitar ROLE_ADMIN si el usuario autenticado es el mismo que se está modificando y tenía el rol.
        if (command.userId().equals(authenticatedUserId) && adminRoleRemoved) {
             throw new IllegalStateException("Acceso denegado: No puedes remover el rol '" + ADMIN_ROLE + "' de tu propia cuenta.");
        }
        
        // 2. Determinar cambios: roles a añadir y roles a eliminar
        
        // Roles a eliminar (Están en el actual, pero no en el nuevo)
        Set<String> rolesToRemove = currentRoleNames.stream()
                .filter(roleName -> !newRoleNames.contains(roleName))
                .collect(Collectors.toSet());

        // Roles a añadir (Están en el nuevo, pero no en el actual)
        Set<String> rolesToAdd = newRoleNames.stream()
                .filter(roleName -> !currentRoleNames.contains(roleName))
                .collect(Collectors.toSet());
        
        // 3. Ejecutar cambios en el Adaptador
        
        // Eliminación eficiente
        if (!rolesToRemove.isEmpty()) {
            rolesPortOut.removeSpecificRolesFromUser(command.userId(), rolesToRemove.stream().toList());
        }
        
        // Inserción eficiente
        if (!rolesToAdd.isEmpty()) {
            rolesPortOut.assignRolesToUser(command.userId(), rolesToAdd.stream().toList());
        }
	}
	
	/**
	 * Creates the new role.
	 *
	 * @param roleDto the role dto
	 */
	@Override
	public void createNewRole(RoleCreationDto roleDto) {
	    if (roleDto.name() == null || roleDto.name().trim().isEmpty()) {
	        throw new IllegalArgumentException("El nombre del rol no puede estar vacío.");
	    }
	    
	    // 1. Estandarizar el nombre: Mayúsculas y prefijo
	    String finalRoleName = "ROLE_" + roleDto.name().trim().toUpperCase();
	    
	    // 2. Persistir (pasando la descripción)
	    rolesPortOut.saveRole(finalRoleName, roleDto.description());
	}
	
	/**
	 * Delete role.
	 *
	 * @param roleName the role name
	 */
	@Override
	public void deleteRole(String roleName) {
	    // 1. Validar que no se intente borrar un rol predeterminado
	    if (ADMIN_ROLE.equals(roleName) || USER_ROLE.equals(roleName)) {
	        throw new IllegalStateException("Acceso denegado: No se pueden eliminar los roles predeterminados ('" + ADMIN_ROLE + "' y '" + USER_ROLE + "').");
	    }

	    // Opcional: Validar que el rol exista y que ningún usuario lo tenga asignado actualmente
	    
	    // 2. Persistir la eliminación
	    rolesPortOut.removeRole(roleName);
	}
}