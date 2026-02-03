package com.peluqueria.api.application.ports.in;

import java.util.List;

import com.peluqueria.api.adapter.out.persistence.addresses.AddressEntity.AddressType;
import com.peluqueria.api.domain.Address;

/**
 * The Interface AddressPortIn.
 */
public interface AddressPortIn {
	
	/**
	 * Load.
	 *
	 * @param id the id
	 * @return the addresses of the user
	 */
	List<Address> load(Long id);
	
	/**
	 * Save.
	 *
	 * @param address the address
	 * @return the address
	 */
	Address save(Address address);

	/**
	 * Update.
	 *
	 * @param address the address
	 * @return the address
	 */
	Address update(Address address);

    /**
	 * Delete.
	 *
	 * @param userId the user id
	 * @param addressId the address id
	 */
    void delete(Long userId, Long addressId);
    
    /**
	 * Load.
	 *
	 * @param userId the user id
	 * @param addressId the address id
	 * @param type the type
	 */
    void setDefault(Long userId, Long addressId, AddressType type);
}
