package com.jwcinema.screen.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Screen {

    private String movieTitle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long price;

    public static Screen createScreen(String movieTitle, LocalDateTime startAt, Long price, Integer playTime, LocalDateTime movieInsertDate) {
       isRegisterAvailable(movieInsertDate, startAt);
       return Screen.builder()
               .movieTitle(movieTitle)
               .price(price)
               .startAt(startAt)
               .endAt(startAt.plusMinutes(playTime))
               .build();
    }

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

    public ScreenEntity toEntity() {
        return ScreenEntity.builder()
                .movieTitle(this.getMovieTitle())
                .price(this.getPrice())
                .startAt(this.getStartAt())
                .endAt(this.getEndAt())
                .build();
    }

    private static void isRegisterAvailable(LocalDateTime insertDate, LocalDateTime startAt){
        if(startAt.isBefore(LocalDateTime.now())){
            throw new ScreenRegisterException("상영 시작 시간을 과거로 등록 할 수 없습니다.");
        }
        if(insertDate.toLocalDate().isEqual(LocalDate.now())){
            throw new ScreenRegisterException("당일 등록한 영화는, 상영시간표에 등록할 수 없습니다.");
        }
    }
}
