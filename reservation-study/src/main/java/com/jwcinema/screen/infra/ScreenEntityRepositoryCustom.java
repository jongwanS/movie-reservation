package com.jwcinema.screen.infra;

import com.jwcinema.screen.domain.Screen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ScreenEntityRepositoryCustom {
//    Optional<Screen> selectMovieTitleAndStartAtAndEndAt(String movieTitle, LocalDateTime startAt, LocalDateTime endAt);

    @Query("SELECT new com.jwcinema.screen.domain.Screen(movieTitle, startAt, endAt, price) FROM ScreenEntity WHERE movieTitle = :movieTitle AND startAt = :startAt AND endAt = :endAt")
    Optional<Screen> selectMovieTitleAndStartAtAndEndAt(@Param("movieTitle") String movieTitle, @Param("startAt") LocalDateTime startAt, @Param("endAt")LocalDateTime endAt);
}
