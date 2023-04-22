package com.jwcinema.screen.domain;

import com.jwcinema.ticketing.domain.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SequenceGenerator(
        name = "MOVIE_TIMETABLE_SEQ_GENERATOR",
        sequenceName = "MOVIE_TIMETABLE_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SCREEN")
public class ScreenEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOVIE_TIMETABLE_SEQ_GENERATOR")
    private Long id;
    @Column(name = "movie_id")
    private Long moveId;
    @Column(name = "price")
    private Long price;
    @Column(name = "seat_limit")
    private Integer seatLimit;
    @Column(name = "seat_reserved_count")
    private Integer seatReservedCount;
    @Column(name = "start_at")
    private LocalDateTime startAt;
    @Column(name = "end_at")
    private LocalDateTime endAt;

    public void registerAvailable(LocalDateTime insertDate) throws Exception {
        if(insertDate.toLocalDate().isEqual(LocalDate.now())){
            throw new Exception("당일 등록한 영화는, 당일 상영시간표에 등록할 수 없습니다.");
        }
    }

    public Screen toScreen() {
        return Screen.builder()
                .id(this.id)
                .movieId(this.moveId)
                .seatLimit(this.seatLimit)
                .seatReservedCount(this.seatReservedCount)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .build();
    }
}
