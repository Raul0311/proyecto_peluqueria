package com.example.demo.domain.dto;

import java.time.LocalTime;

/**
 * The Record TimeRangeDto.
 *
 * @param start the start
 * @param end the end
 */
public record TimeRangeDto(LocalTime start, LocalTime end) {}