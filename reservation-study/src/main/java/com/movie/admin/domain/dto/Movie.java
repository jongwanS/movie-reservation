package com.movie.admin.domain.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Movie {
    private Long id;
    private String title;
    private String director;
    private String actor;
    private String description;
    private Long price;
    private LocalDateTime insertDate;

    public boolean registerAvailable() {
        return this.insertDate
                .isBefore(LocalDateTime.now()
                        .minusDays(1));
    }
}
