package com.example.demo.adapter.out.persistence.appointment;

import com.example.demo.domain.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The Interface AppointmentMapper.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    /**
     * To domain.
     *
     * @param entity the entity
     * @return the appointment
     */
    // Eliminamos los mappings que apuntan a "user.name" y "user.email"
    // para evitar que MapStruct intente inicializar el Proxy perezoso de Hibernate.
    // MapStruct usará automáticamente los campos clientFullName y clientEmail de AppointmentEntity.
    Appointment toDomain(AppointmentEntity entity);

    /**
     * To entity.
     *
     * @param domain the domain
     * @return the appointment entity
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AppointmentEntity toEntity(Appointment domain);
}