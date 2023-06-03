package com.jwcinema.movie.domain;

import com.jwcinema.movie.controller.dto.MovieRegisterException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Objects;


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

    @Builder
    public Movie(String title, Integer playtime, String description) {
        validate();
        this.title = title;
        this.playtime = playtime;
        this.description = description;
    }


    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public MovieEntity toEntity() {
        return MovieEntity.builder()
                .title(title)
                .playtime(playtime)
                .description(description)
                .insertDate(LocalDateTime.now())
                .build();
    }

    public void validate(){
        if(ObjectUtils.isEmpty(title)){
            throw new MovieRegisterException("title 은 필수값");
        }
        if(ObjectUtils.isEmpty(playtime)){
            throw new MovieRegisterException("playtime 필수값");
        }
    }
}
