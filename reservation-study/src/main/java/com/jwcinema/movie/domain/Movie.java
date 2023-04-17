package com.jwcinema.movie.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Movie {
    private Long id;
    private String title;
    private Long playtime;
    private String description;
    private LocalDateTime insertDate;

    public boolean registerAvailable() {
        return this.insertDate
                .isBefore(LocalDateTime.now()
                        .minusDays(1));
    }
}
