package com.jwcinema.movie.application;

import com.jwcinema.movie.controller.MovieRegisterRequest;
import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieEntityRepository movieEntityRepository;

    public MovieEntity registerMovie(MovieRegisterRequest movieRegisterRequest) {
        MovieEntity movieEntity = MovieEntity.builder()
                .title(movieRegisterRequest.getTitle())
                .playtime(movieRegisterRequest.getPlaytime())
                .description(movieRegisterRequest.getDescription())
                .insertDate(LocalDateTime.now())
                .build();
        return movieEntityRepository.save(movieEntity);
    }

}