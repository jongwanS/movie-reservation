package com.jwcinema.ticketing.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Screen {
    private String movieTitle;
    private Long price;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Builder
    public Screen(String movieTitle, Long price, LocalDateTime startAt, LocalDateTime endAt) {
        this.movieTitle = movieTitle;
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
