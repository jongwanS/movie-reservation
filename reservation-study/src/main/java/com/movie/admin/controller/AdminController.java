package com.movie.admin.controller;

import com.movie.admin.application.MovieService;
import com.movie.admin.domain.dto.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "admin")
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;

    @PostMapping("/movie/register")
    public ResponseEntity<?> movieRegister(
            @RequestBody MovieRegisterRequest movieRegisterRequest
    ){
        movieRegisterRequest.validate();

        movieService.registerMovie(movieRegisterRequest);

        return ResponseEntity.ok().body("영화 등록 성공");
    }

    @PostMapping("/movie/timetable/register")
    public ResponseEntity<?> timetableRegister(
        @RequestBody MovieTimetableRegisterRequest movieTimetableRegisterRequest
    ) throws Exception {
        movieService.registerMovieTimeTable(movieTimetableRegisterRequest);
        return ResponseEntity.ok().body("영화 상영 시간 등록 성공");
    }
}
