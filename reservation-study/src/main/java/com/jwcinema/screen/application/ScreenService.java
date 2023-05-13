package com.jwcinema.screen.application;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.controller.dto.ScreenRegisterRequest;
import com.jwcinema.screen.domain.Screen;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.domain.ScreenRegisterException;
import com.jwcinema.screen.domain.ScreenScheduleExistException;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final MovieEntityRepository movieEntityRepository;
    private final ScreenEntityRepository screenEntityRepository;

    public Screen register(ScreenRegisterRequest screenRegisterRequest) {

        MovieEntity movie = movieEntityRepository.findByTitle(screenRegisterRequest.getMovieTitle())
                .orElseThrow(() -> new ScreenRegisterException(screenRegisterRequest.getMovieTitle()+" 로 등록된 영화가 없습니당 "));

        screenEntityRepository.findByStartAtAndEndAt(screenRegisterRequest.getStartAt(),
                screenRegisterRequest.getStartAt().plusMinutes(movie.getPlaytime())).ifPresent(entity -> {
            throw new ScreenScheduleExistException("이미 등록된 상영 예정영화가 존재하여, 상영등록 할 수 없습니다..");
        });

        ScreenEntity screenEntity = ScreenEntity.builder()
                .movieId(movie.getId())
                .price(screenRegisterRequest.getPrice())
                .startAt(screenRegisterRequest.getStartAt())
                .endAt(screenRegisterRequest.getStartAt().plusMinutes(movie.getPlaytime()))
                .build();
        screenEntity.isRegisterAvailable(movie.getInsertDate());


        ScreenEntity savedScreenEntity = screenEntityRepository.save(screenEntity);

        return Screen.builder()
                .movieTitle(screenRegisterRequest.getMovieTitle())
                .startAt(savedScreenEntity.getStartAt())
                .endAt(savedScreenEntity.getEndAt())
                .price(savedScreenEntity.getPrice())
                .build();
    }
}
