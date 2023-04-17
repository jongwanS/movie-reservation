package com.jwcinema.movie.controller;

import com.jwcinema.movie.application.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/movie/register")
    public ResponseEntity<?> movieRegister(
            @RequestBody MovieRegisterRequest movieRegisterRequest
    ) throws Exception {
        movieRegisterRequest.validate();
        movieService.registerMovie(movieRegisterRequest);
        return ResponseEntity.ok().body("영화 등록 성공");
    }
}
