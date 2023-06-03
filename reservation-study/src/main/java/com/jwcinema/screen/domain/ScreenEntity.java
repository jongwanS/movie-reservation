package com.jwcinema.screen.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@SequenceGenerator(
        name = "MOVIE_TIMETABLE_SEQ_GENERATOR",
        sequenceName = "MOVIE_TIMETABLE_SEQ",
        allocationSize = 1
)
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Builder
@Table(name = "SCREEN")
public class ScreenEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_TIMETABLE_SEQ_GENERATOR")
    private Long id;
    @Column(name = "movie_title")
    private String movieTitle;
    @Column(name = "price")
    private Long price;
    @Column(name = "start_at")
    private LocalDateTime startAt;
    @Column(name = "end_at")
    private LocalDateTime endAt;

}
