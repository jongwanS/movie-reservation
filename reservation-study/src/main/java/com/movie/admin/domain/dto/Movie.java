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
    private String cast;
    private String description;
    private Long price;
    private LocalDateTime insertDate;
}
