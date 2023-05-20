package com.jwcinema.movie.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@SequenceGenerator(
        name = "MOVIE_SEQ_GENERATOR",
        sequenceName = "MOVIE_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MOVIE")
public class MovieEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_SEQ_GENERATOR")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "playtime")
    private Integer playtime;
    @Column(name = "description")
    private String description;
    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    public Movie toMovie() {
        return Movie.builder()
                .title(this.title)
                .playtime(this.playtime)
                .description(this.description)
                .build();
    }
}
