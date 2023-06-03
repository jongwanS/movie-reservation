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

        Screen screen = Screen.createScreen(
                movie.getTitle()
                , screenRegisterRequest.getStartAt()
                , screenRegisterRequest.getPrice()
                , movie.getPlaytime()
                , movie.getInsertDate()
        );
        screenEntityRepository.save(screen.toEntity());
        return screen;
    }
}
