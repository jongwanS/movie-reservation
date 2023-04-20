package com.jwcinema.movie.controller;

import com.jwcinema.movie.application.MovieService;
import com.jwcinema.movie.domain.MovieEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/register")
    public ResponseEntity<?> movieRegister(
            @RequestBody MovieRegisterRequest movieRegisterRequest
    ) {
        try {
            movieRegisterRequest.validate();
            MovieEntity movie = movieService.registerMovie(movieRegisterRequest);
            return ResponseEntity.ok().body(movie);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
