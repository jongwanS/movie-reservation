package com.jwcinema.screen.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class Screen {

    private String movieTitle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Long price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screen screen = (Screen) o;
        return movieTitle.equals(screen.movieTitle) && startAt.equals(screen.startAt) && endAt.equals(screen.endAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieTitle, startAt, endAt);
    }
}
