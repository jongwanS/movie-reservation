package com.movie.admin.domain.entity;

import com.movie.admin.domain.dto.Movie;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "director")
    private String director;
    @Column(name = "actor")
    private String actor;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Long price;
    @Column(name = "duration")
    private Long second;
    @Column(name = "insert_date")
    private LocalDateTime insertDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public Movie toDto(MovieEntity movieEntity){
        return Movie.builder()
                .build();
    }
    public boolean registerAvailable() {
        return this.insertDate
                .isBefore(LocalDateTime.now()
                        .minusDays(1));
    }
}
