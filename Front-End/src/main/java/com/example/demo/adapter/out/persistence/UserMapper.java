package com.example.demo.adapter.out.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.adapter.out.persistence.roles.RoleMapper;
import com.example.demo.domain.User;

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
}
