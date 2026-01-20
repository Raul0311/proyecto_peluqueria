package com.example.demo.adapter.out.persistence.roles;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.domain.Role;
import com.example.demo.domain.dto.RoleDto;

/**
 * The Interface RoleMapper.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

	/**
	 * To domain.
	 *
	 * @param role the role
	 * @return the role
	 */
	@Mapping(target = "name", source = "roleName")
    Role toDomain(RoleEntity role);
	
	/**
	 * To dto.
	 *
	 * @param role the role
	 * @return the role dto
	 */
	@Mapping(target = "name", source = "roleName")
    RoleDto toDto(RoleEntity role);

    /**
     * To entity.
     *
     * @param role the role
     * @return the role entity
     */
    @Mapping(target = "roleName", source = "name")
    RoleEntity toEntity(Role role);
    
    /**
     * To dto list.
     *
     * @param entities the entities
     * @return the list
     */
    List<RoleDto> toDtoList(List<RoleEntity> entities);
}