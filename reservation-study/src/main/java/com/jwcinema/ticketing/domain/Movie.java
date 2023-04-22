package com.jwcinema.ticketing.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Movie {
    private Long id;
    private String title;
    private Integer playtime;
    private String description;
    private LocalDateTime insertDate;
}
