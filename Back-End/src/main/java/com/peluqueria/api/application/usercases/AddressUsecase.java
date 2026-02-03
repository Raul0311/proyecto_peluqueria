package com.peluqueria.api.application.usercases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.peluqueria.api.adapter.out.persistence.addresses.AddressEntity.AddressType;
import com.peluqueria.api.application.ports.in.AddressPortIn;
import com.peluqueria.api.application.ports.out.AddressPortOut;
import com.peluqueria.api.domain.Address;

import lombok.RequiredArgsConstructor;

/**
 * The Class AddressUsecase.
 */
@Service
@RequiredArgsConstructor
public class AddressUsecase implements AddressPortIn {
	
	/** The address port out. */
	private final AddressPortOut addressPortOut;

	/**
	 * Load.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	@Override
	public List<Address> load(Long userId) {
		
		return addressPortOut.load(userId);
	}

	/**
	 * Save.
	 *
	 * @param address the address
	 * @return the address
	 */
	@Override
    public Address save(Address address) {
		if (address.getUserId() == null) {
            throw new IllegalArgumentException("userId es obligatorio en la dirección");
        }
		
        return addressPortOut.save(address);
    }

    /**
     * Update.
     *
     * @param address the address
     * @return the address
     */
    @Override
    public Address update(Address address) {
    	if (address.getUserId() == null || address.getId() == null) {
            throw new IllegalArgumentException("userId y id de dirección son obligatorios");
        }
    	
        return addressPortOut.update(address);
    }

    /**
     * Delete.
     *
     * @param userId the user id
     * @param addressId the address id
     */
    @Override
    public void delete(Long userId, Long addressId) {
        addressPortOut.delete(userId, addressId);
    }
    
    /**
     * Sets the default.
     *
     * @param userId the user id
     * @param addressId the address id
     * @param type the type
     */
    @Override
    public void setDefault(Long userId, Long addressId, AddressType type) {
        addressPortOut.setDefault(userId, addressId, type);
    }
}
