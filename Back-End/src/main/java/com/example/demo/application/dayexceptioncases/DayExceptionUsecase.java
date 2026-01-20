package com.example.demo.application.dayexceptioncases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.application.ports.in.DayExceptionPortIn;
import com.example.demo.application.ports.out.DayExceptionPortOut;
import com.example.demo.domain.dto.DayExceptionDto;

import lombok.RequiredArgsConstructor;

/**
 * The Class DayExceptionUsecase.
 */
@Service
@RequiredArgsConstructor
public class DayExceptionUsecase implements DayExceptionPortIn {

    /** The port out. */
    private final DayExceptionPortOut portOut;

    /**
     * Find all exceptions.
     *
     * @return the list
     */
    @Override
    public List<DayExceptionDto> findAllExceptions() {
        return portOut.getAll();
    }

    /**
     * Save exception.
     *
     * @param dto the dto
     */
    @Override
    public void saveException(DayExceptionDto dto) {
        // Lógica: Si ya existe esa fecha, actualizamos. Si no, creamos.
        portOut.upsert(dto);
    }
}