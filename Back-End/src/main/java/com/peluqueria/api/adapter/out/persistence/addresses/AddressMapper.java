package com.peluqueria.api.adapter.out.persistence.addresses;

import org.mapstruct.Mapper;

import com.peluqueria.api.domain.Address;

/**
 * The Interface AddressMapper.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

    /**
     * Convierte una AddressEntity (persistencia) a Address (dominio).
     *
     * @param addressEntity the address entity
     * @return the address
     */
	Address toDomain(AddressEntity addressEntity);

    /**
     * Convierte una Address (dominio) a AddressEntity (persistencia).
     *
     * @param address the address
     * @return the address entity
     */
	AddressEntity toEntity(Address address);
}
