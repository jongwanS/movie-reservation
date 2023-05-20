package com.jwcinema.screen.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
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
@Table(name = "SCREEN")
public class ScreenEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_TIMETABLE_SEQ_GENERATOR")
    private Long id;
    @Column(name = "movie_id")
    private Long movieId;
    @Column(name = "price")
    private Long price;
    @Column(name = "start_at")
    private LocalDateTime startAt;
    @Column(name = "end_at")
    private LocalDateTime endAt;


    @Builder
    public ScreenEntity(Long movieId, Long price, LocalDateTime startAt, LocalDateTime endAt, LocalDateTime movieInsertDate) {
        this.movieId = movieId;
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
        isRegisterAvailable(movieInsertDate);
    }

    private void isRegisterAvailable(LocalDateTime insertDate){
        if(this.startAt.isBefore(LocalDateTime.now())){
            throw new ScreenRegisterException("상영 시작 시간을 과거로 등록 할 수 없습니다.");
        }
        if(insertDate.toLocalDate().isEqual(LocalDate.now())){
            throw new ScreenRegisterException("당일 등록한 영화는, 상영시간표에 등록할 수 없습니다.");
        }
    }
}
