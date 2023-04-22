package com.jwcinema.screen.application;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.controller.ScreenRegisterRequest;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final MovieEntityRepository movieEntityRepository;
    private final ScreenEntityRepository screenEntityRepository;

    public ScreenEntity registerScreenSchedule(ScreenRegisterRequest screenRegisterRequest) throws Exception {

        MovieEntity movie = movieEntityRepository.findById(screenRegisterRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException(screenRegisterRequest.getMovieId()+" 로 등록된 영화가 없습니당 "));

        ScreenEntity screen = ScreenEntity.builder()
                .moveId(movie.getId())
                .seatLimit(screenRegisterRequest.getSeatLimit().orElse(10))
                .price(screenRegisterRequest.getPrice())
                .startAt(screenRegisterRequest.getStartAt())
                .endAt(screenRegisterRequest.getEndAt())
                .build();

        screen.registerAvailable(movie.getInsertDate());
        return screenEntityRepository.save(screen);
    }
}
