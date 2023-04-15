package com.movie.admin.application;

import com.movie.admin.controller.MovieRegisterRequest;
import com.movie.admin.domain.entity.MovieEntity;
import com.movie.admin.infra.MovieEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieEntityRepository movieEntityRepository;

    public void registerMovie(MovieRegisterRequest movieRegisterRequest) {
        MovieEntity movieEntity = MovieEntity.builder()
                .title(movieRegisterRequest.getTitle())
                .director(movieRegisterRequest.getDirector())
                .cast(movieRegisterRequest.getCast())
                .description(movieRegisterRequest.getDescription())
                .price(movieRegisterRequest.getPrice())
                .build();
        movieEntityRepository.save(movieEntity);
    }

    public void registerMoiveTimeTable() {
    }
}
