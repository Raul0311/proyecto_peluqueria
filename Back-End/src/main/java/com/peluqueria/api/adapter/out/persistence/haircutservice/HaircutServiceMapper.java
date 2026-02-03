package com.peluqueria.api.adapter.out.persistence.haircutservice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.peluqueria.api.domain.HaircutService;

/**
 * The Interface HaircutServiceMapper.
 */
@Mapper(componentModel = "spring")
public interface HaircutServiceMapper {

    /**
     * To domain.
     *
     * @param entity the entity
     * @return the haircut service
     */
    // De Entidad (DB) a Dominio
    HaircutService toDomain(HaircutServiceEntity entity);

    /**
     * To entity.
     *
     * @param domain the domain
     * @return the haircut service entity
     */
    // De Dominio a Entidad (DB)
    @Mapping(target = "id", source = "id")
    HaircutServiceEntity toEntity(HaircutService domain);
}