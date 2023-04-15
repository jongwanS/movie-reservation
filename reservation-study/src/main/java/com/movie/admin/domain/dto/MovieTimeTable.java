package com.movie.admin.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MovieTimeTable {
    private Long id;
    private Long movieId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
