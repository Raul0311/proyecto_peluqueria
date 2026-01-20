package com.example.demo.adapter.out.persistence.dayexception;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.application.ports.out.DayExceptionPortOut;
import com.example.demo.domain.dto.DayExceptionDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class DayExceptionAdapterOut.
 */
@Component
@RequiredArgsConstructor
public class DayExceptionAdapterOut implements DayExceptionPortOut {

    /** The repository. */
    private final DayExceptionRepository repository;

    /**
     * Gets the all.
     *
     * @return the all
     */
    @Override
    public List<DayExceptionDto> getAll() {
        return repository.findAll().stream().map(entity -> {
            DayExceptionDto dto = new DayExceptionDto();
            dto.setDate(entity.getDate().toString());
            dto.setStartTime(entity.getStartTime().toString());
            dto.setEndTime(entity.getEndTime().toString());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Upsert.
     *
     * @param dto the dto
     */
    @Override
    public void upsert(DayExceptionDto dto) {
        LocalDate date = LocalDate.parse(dto.getDate());
        
        DayExceptionEntity entity = repository.findByDate(date)
                .orElse(new DayExceptionEntity());
        
        entity.setDate(date);
        entity.setStartTime(LocalTime.parse(dto.getStartTime()));
        entity.setEndTime(LocalTime.parse(dto.getEndTime()));
        
        repository.save(entity);
    }
}