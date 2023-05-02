package com.jwcinema.movie.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@Getter
public class Movie {
    private String title;
    private Integer playtime;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return title.equals(movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
