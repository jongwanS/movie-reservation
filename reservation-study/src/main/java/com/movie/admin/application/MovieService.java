package com.movie.admin.application;

import com.movie.admin.controller.MovieRegisterRequest;
import com.movie.admin.controller.MovieTimetableRegisterRequest;
import com.movie.admin.domain.entity.MovieEntity;
import com.movie.admin.domain.entity.MovieTimeTableEntity;
import com.movie.admin.infra.MovieEntityRepository;
import com.movie.admin.infra.MovieTimeTableEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieEntityRepository movieEntityRepository;
    private final MovieTimeTableEntityRepository movieTimeTableEntityRepository;

    public void registerMovie(MovieRegisterRequest movieRegisterRequest) {
        MovieEntity movieEntity = MovieEntity.builder()
                .title(movieRegisterRequest.getTitle())
                .director(movieRegisterRequest.getDirector())
                .actor(movieRegisterRequest.getActor())
                .description(movieRegisterRequest.getDescription())
                .price(movieRegisterRequest.getPrice())
                .build();
        movieEntityRepository.save(movieEntity);
    }

    public void registerMovieTimeTable(MovieTimetableRegisterRequest timetableRegister) throws Exception {
        MovieEntity movie = movieEntityRepository.findById(timetableRegister.getMovieId())
                .orElseThrow(()-> new Exception());
        if(!movie.registerAvailable()){
            throw new Exception();
        }
        MovieTimeTableEntity movieTimeTable = MovieTimeTableEntity.builder()
                .movieId(movie.getId())
                .startDate(timetableRegister.getStartDate())
                .endDate(timetableRegister.getEndDate())
                .build();
        movieTimeTableEntityRepository.save(movieTimeTable);
    }
}
