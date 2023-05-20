package com.jwcinema.screen.infra;

import com.jwcinema.screen.domain.Screen;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ScreenEntityRepositoryCustom {
    Optional<Screen> selectMovieTitleAndStartAtAndEndAt(String movieTitle, LocalDateTime startAt, LocalDateTime endAt);
}
