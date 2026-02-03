package com.peluqueria.api.adapter.out.persistence.addresses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class AddressEntity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class AddressEntity {
    
    /**
     * The Enum AddressType.
     */
    public enum AddressType {
        
        /** The billing. */
        BILLING,
        
        /** The shipping. */
        SHIPPING
    }

	/** The id. */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	/** The type. */
	@Enumerated(EnumType.STRING)
    private AddressType type;
	
	/** The name. */
	@Column(name = "name", nullable = false)
    private String name;
	
	/** The lastname 1. */
	@Column(name = "lastname1", nullable = false)
    private String lastname1;
	
	/** The lastname 2. */
	@Column(name = "lastname2", nullable = false)
    private String lastname2;

	/** The street. */
	@Column(name = "street", nullable = false)
    private String street;

    /** The number address. */
    @Column(name = "number_address", nullable = false)
    private String numberAddress;

    /** The apartment. */
    @Column(name = "apartment")
    private String apartment;

    /** The city. */
    @Column(name = "city", nullable = false)
    private String city;

    /** The zip code. */
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    /** The country. */
    @Column(name = "country", nullable = false)
    private String country;

    /** The user id. */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** The predeterminated. */
    @Column(name = "predeterminated")
    private Boolean predeterminated;
}
