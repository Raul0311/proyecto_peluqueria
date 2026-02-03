package com.peluqueria.api.application.ports.out;

import java.util.List;

import com.peluqueria.api.adapter.out.persistence.addresses.AddressEntity.AddressType;
import com.peluqueria.api.domain.Address;

/**
 * The Interface UserPortOut.
 */
public interface AddressPortOut {
	
	/**
	 * Load.
	 *
	 * @param userId the user id
	 * @return the addresses
	 */
	List<Address> load(Long userId);
	
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
	 * @return the addresses
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
	 * SetDefault.
	 *
	 * @param userId the user id
	 * @param addressId the address id
	 * @param type the type
	 */
    void setDefault(Long userId, Long addressId, AddressType type);
}
