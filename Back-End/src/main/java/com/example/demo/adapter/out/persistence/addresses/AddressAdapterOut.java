package com.example.demo.adapter.out.persistence.addresses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.adapter.out.persistence.addresses.AddressEntity.AddressType;
import com.example.demo.application.ports.out.AddressPortOut;
import com.example.demo.domain.Address;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * The Class UserAdapterOut.
 */
@Service
@RequiredArgsConstructor
public class AddressAdapterOut implements AddressPortOut {
	
	/**
	 * The Class AddressSaveException.
	 */
	@SuppressWarnings("serial")
    public class AddressSaveException extends RuntimeException {
        
        /**
         * Instantiates a new address save exception.
         *
         * @param message the message
         */
        public AddressSaveException(String message) {
            super(message);
        }
    }

    /**
     * The Class AddressUpdateException.
     */
    @SuppressWarnings("serial")
    public class AddressUpdateException extends RuntimeException {
        
        /**
         * Instantiates a new address update exception.
         *
         * @param message the message
         */
        public AddressUpdateException(String message) {
            super(message);
        }
    }

    /**
     * The Class AddressDeleteException.
     */
    @SuppressWarnings("serial")
    public class AddressDeleteException extends RuntimeException {
        
        /**
         * Instantiates a new address delete exception.
         *
         * @param message the message
         */
        public AddressDeleteException(String message) {
            super(message);
        }
    }
    
    /**
     * The Class AddressDefaultChangeException.
     */
    @SuppressWarnings("serial")
    public class AddressDefaultChangeException extends RuntimeException {
        
        /**
         * Instantiates a new address default change exception.
         *
         * @param message the message
         */
        public AddressDefaultChangeException(String message) {
            super(message);
        }
    }

	/** The address repository. */
	private final AddressRepository addressRepository;
	
	/** The address mapper. */
	private final AddressMapper addressMapper;

    /**
     * Load.
     *
     * @param userId the user id
     * @return the list
     */
    @Override
    public List<Address> load(Long userId) {
        List<AddressEntity> addressesEntity = addressRepository.findByUserId(userId);

        List<Address> addresses = new ArrayList<>();

        for (AddressEntity entity : addressesEntity) {
            addresses.add(addressMapper.toDomain(entity));
        }
        
        return addresses;
    }

    /**
     * Save.
     *
     * @param address the address
     * @return the address
     */
    @Override
    @Transactional
    public Address save(Address address) {
        
        if (address.getUserId() == null) {
            throw new IllegalArgumentException("userId es obligatorio en la dirección");
        }
        
        if (Boolean.TRUE.equals(address.getPredeterminated())) {
            // Desmarcar la anterior predeterminada del mismo tipo
            addressRepository.clearDefault(address.getUserId(), address.getType().name());
        }
        Address newAddress = addressMapper.toDomain(addressRepository.save(addressMapper.toEntity(address)));
        if(newAddress == null) throw new AddressSaveException("No se pudo guardar la nueva dirección.");
        
        return newAddress;
    }

    /**
     * Update.
     *
     * @param address the address
     * @return the address
     */
    @Override
    @Transactional
    public Address update(Address address) {
        if (!addressRepository.existsById(address.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }
        
        if (Boolean.TRUE.equals(address.getPredeterminated())) {
            // Desmarcar la anterior predeterminada del mismo tipo
            addressRepository.clearDefault(address.getUserId(), address.getType().name());
        }
        
        Address updatedAddress = addressMapper.toDomain(addressRepository.save(addressMapper.toEntity(address)));
        if(updatedAddress == null) throw new AddressUpdateException("No se pudo actualizar la dirección.");
        
        return updatedAddress;
    }

    /**
     * Delete.
     *
     * @param userId the user id
     * @param addressId the address id
     */
    @Override
    @Transactional
    public void delete(Long userId, Long addressId) {
        AddressEntity addr = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        if (Boolean.TRUE.equals(addr.getPredeterminated())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete default address");
        }
        
        try {
        	addressRepository.deleteById(addressId);
        } catch(AddressDeleteException e) {
    		throw new AddressDeleteException("Fallo al intentar eliminar la dirección con ID: " + addressId);
    	}
    }
    
    /**
     * Sets the default.
     *
     * @param userId the user id
     * @param addressId the address id
     * @param type the type
     */
    @Override
    @Transactional
    public void setDefault(Long userId, Long addressId, AddressType type) {
    	Integer result = addressRepository.setDefaultAddress(addressId, userId, type.name());
        if(result == 0) throw new AddressDefaultChangeException("No se pudo establecer la dirección " + addressId + " como predeterminada.");
    }
}