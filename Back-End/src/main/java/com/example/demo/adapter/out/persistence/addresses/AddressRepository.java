package com.example.demo.adapter.out.persistence.addresses;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

/**
 * The Interface AddressRepository.
 */
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
	
	/**
	 * Find by userId.
	 *
	 * @param userId the userId
	 * @return the addresses
	 */
	@Query(value="SELECT * FROM addresses ad WHERE ad.user_id = ?1", nativeQuery = true)
	List<AddressEntity> findByUserId(Long userId);
	
	/**
	 * Delete by id.
	 *
	 * @param id the id
	 */
	@Modifying
    @Transactional
    @Query(value = "DELETE FROM addresses WHERE id = ?1", nativeQuery = true)
    void deleteById(Long id);
	
	/**
	 * Set predeterminated Address.
	 *
	 * @param addressId the address id
	 * @param userId the user id
	 * @param type the type
	 * @return the integer
	 */
	@Modifying
    @Transactional
    @Query(value = "UPDATE addresses SET predeterminated = CASE WHEN id = ?1 THEN TRUE ELSE FALSE END WHERE user_id = ?2 AND type = ?3", nativeQuery = true)
    Integer setDefaultAddress(Long addressId, Long userId, String type);
	
	/**
	 * Clear predeterminated Address.
	 *
	 * @param userId the user id
	 * @param type the type
	 * @return the integer
	 */
	@Modifying
    @Transactional
	@Query(value = "UPDATE addresses SET predeterminated = false WHERE user_id = ?1 AND type = ?2 AND predeterminated = true", nativeQuery = true)
	Integer clearDefault(Long userId, String type);
}