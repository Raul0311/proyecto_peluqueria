package com.peluqueria.front_end.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.peluqueria.front_end.adapter.out.persistence.roles.RoleMapper;
import com.peluqueria.front_end.adapter.out.persistence.roles.UserRolesEntity;
import com.peluqueria.front_end.domain.Role;
import com.peluqueria.front_end.domain.User;

/**
 * Mapper between UserEntity, UserDTO and User domain object.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface UserMapper {

    /**
     * Entity to domain.
     *
     * @param userEntity the user entity
     * @return the user domain object
     */
	@Mapping(target = "userToken", ignore = true)
    @Mapping(target = "rolesStr", ignore = true)
    @Mapping(target = "passw", ignore = true)
	@Mapping(target = "roles", source = "userRoles", qualifiedByName = "mapUserRolesToRoles")
    User toDomain(UserEntity userEntity);

    /**
     * Domain to entity.
     *
     * @param user the user domain object
     * @return the user entity
     */
	@Mapping(target = "userRoles", ignore = true)
    UserEntity toEntity(User user);
	
	// Método auxiliar para MapStruct: convierte List<UserRolesEntity> → List<Role>
    @Named("mapUserRolesToRoles")
    default List<Role> mapUserRolesToRoles(List<UserRolesEntity> userRoles) {
        if (userRoles == null) return List.of();
        return userRoles.stream()
                        .map(UserRolesEntity::getRole)   // List<RoleEntity>
                        .map(RoleEntity -> {
                            Role r = new Role();
                            r.setId(RoleEntity.getId());
                            r.setName(RoleEntity.getRoleName());
                            r.setDescription(RoleEntity.getDescription());
                            return r;
                        })
                        .collect(Collectors.toList());
    }
}
