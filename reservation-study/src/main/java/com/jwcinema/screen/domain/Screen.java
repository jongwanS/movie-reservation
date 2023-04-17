package com.jwcinema.screen.domain;

import com.jwcinema.movie.domain.Movie;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Screen {
    private Long id;
    private Movie movie;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
