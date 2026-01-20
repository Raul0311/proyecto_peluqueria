package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    /** The Constant INTERNAL_SERVER_ERROR. */
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    /** The id. */
    private Long id;
    
    /** The username. */
    private String username;
    
    /** The passw. */
    private String passw;
    
    /** The name. */
    private String name;
    
    /** The lastname 1. */
    private String lastname1;
    
    /** The lastname 2. */
    private String lastname2;
    
    /** The city. */
    private String city;
    
    /** The country. */
    private String country;
    
    /** The street. */
    private String street;
    
    /** The number address. */
    private String numberAddress;
    
    /** The apartment. */
    private String apartment;
    
    /** The zip code. */
    private String zipCode;
    
    /** The phone. */
    private String phone;
    
    /** The email. */
    private String email;
    
    /** The enabled. */
    private Boolean enabled;
    
    /** The last login. */
    private LocalDateTime lastLogin;
    
    /** The created at. */
    private LocalDateTime createdAt;
    
    /** The updated at. */
    private LocalDateTime updatedAt;
    
    /** The user token. */
    private String userToken;
    
    /** The roles str. */
    private String rolesStr;

    /** The roles. */
    private List<Role> roles = new ArrayList<>();
}

