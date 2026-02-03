package com.peluqueria.api.adapter.out.persistence.roles;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The Interface RoleRepository.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
	/**
	 * Find role by name.
	 *
	 * @param name the name
	 * @return if the userToken is validate
	 */
	@Query(value="SELECT * FROM roles WHERE role_name = ?1", nativeQuery = true)
	Optional<RoleEntity> findByName(String name);
	
	/**
	 * Find id by role names.
	 *
	 * @param roleNames the role names
	 * @return the ids of roles
	 */
	@Query("SELECT r.id FROM RoleEntity r WHERE r.roleName IN :roleNames")
    List<Long> findIdsByRoleNames(@Param("roleNames") List<String> roleNames);
	
	/**
	 * Delete role by its name.
	 *
	 * @param roleName the role name
	 */
    @Modifying
    @Query("DELETE FROM RoleEntity r WHERE r.roleName = :roleName")
    void deleteByRoleName(@Param("roleName") String roleName);
    
    /**
     * Find role by name.
     *
     * @param roleNames the role names
     * @return the list
     */
    @Query("SELECT r FROM RoleEntity r WHERE r.roleName IN :roleNames")
    List<RoleEntity> findByRoleNameIn(@Param("roleNames") List<String> roleNames);
    
    /**
     * Find all by user id.
     *
     * @param userId the user id
     * @return the list
     */
    @Query(value = "SELECT r.role_name FROM roles r " +
            "JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = ?1", nativeQuery = true)
    List<String> findAllByUserId(Long userId);
}