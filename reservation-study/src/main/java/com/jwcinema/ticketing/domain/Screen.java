package com.jwcinema.ticketing.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Screen {
    private Long id;
    private Long movieId;
    private Integer seatLimit;
    private Integer seatReservedCount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
