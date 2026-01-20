package com.example.demo.adapter.out.persistence.roles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Interface UserRolesRepository.
 */
// He Reemplazado Long por UserRoleId
public interface UserRolesRepository extends JpaRepository<UserRolesEntity, UserRoleId> {
	
	/**
	 * Delete role by user id.
	 *
	 * @param userId the user id
	 * @param roleIds the role ids
	 * @return the int
	 */
	@Modifying
    @Transactional
    @Query("DELETE FROM UserRolesEntity ur WHERE ur.userId = :userId AND ur.roleId IN :roleIds")
    int deleteByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}