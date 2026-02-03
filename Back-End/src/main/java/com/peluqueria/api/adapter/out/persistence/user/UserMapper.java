package com.peluqueria.api.adapter.out.persistence.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.peluqueria.api.adapter.out.persistence.roles.RoleMapper;
import com.peluqueria.api.domain.User;

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
	@Mapping(target = "roles", source = "roles")
    User toDomain(UserEntity userEntity);

    /**
     * Domain to entity.
     *
     * @param user the user domain object
     * @return the user entity
     */
	@Mapping(target = "userRoles", ignore = true)
    UserEntity toEntity(User user);
	
	/**
	 * Actualiza una instancia existente de UserEntity con los datos de User (dominio).
	 *
	 * @param user the user domain object
	 * @param entity the entity
	 */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "passw", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDomain(User user, @MappingTarget UserEntity entity);
}
