package com.jwcinema.screen.infra;

import com.jwcinema.screen.domain.Screen;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class ScreenEntityRepositoryCustomImpl implements ScreenEntityRepositoryCustom{

    @Override
    public Optional<Screen> selectMovieTitleAndStartAtAndEndAt(String movieTitle, LocalDateTime startAt, LocalDateTime endAt) {
        return Optional.empty();
    }
}
