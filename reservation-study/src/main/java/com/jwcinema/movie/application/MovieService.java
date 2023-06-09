package com.jwcinema.movie.application;

import com.jwcinema.movie.controller.dto.MovieRegisterRequest;
import com.jwcinema.movie.domain.Movie;
import com.jwcinema.movie.domain.MovieAlreadyExistException;
import com.jwcinema.movie.infra.MovieEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieEntityRepository movieEntityRepository;

    public Movie register(MovieRegisterRequest movieRegisterRequest) {

        movieEntityRepository.findByTitle(movieRegisterRequest.getTitle()).ifPresent(entity -> {
            throw new MovieAlreadyExistException("이미 존재하는 영화입니다.");
        });
        Movie movie = Movie.builder()
                .title(movieRegisterRequest.getTitle())
                .playtime(movieRegisterRequest.getPlaytime())
                .description(movieRegisterRequest.getDescription())
                .build();
        movieEntityRepository.save(movie.toEntity());
        return movie;
    }
}
