package com.peluqueria.api.domain;

import com.peluqueria.api.adapter.out.persistence.addresses.AddressEntity.AddressType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class Address.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    /** The id. */
    private Long id;
    
    /** The type. */
    private AddressType type;
    
    /** The name. */
    private String name;
    
    /** The lastname 1. */
    private String lastname1;
    
    /** The lastname 2. */
    private String lastname2;

    /** The street. */
    private String street;

    /** The number address. */
    private String numberAddress;

    /** The apartment. */
    private String apartment;

    /** The city. */
    private String city;

    /** The zip code. */
    private String zipCode;

    /** The country. */
    private String country;

    /** The user id. */
    private Long userId;
    
    /** The predeterminated. */
    private Boolean predeterminated;
}
