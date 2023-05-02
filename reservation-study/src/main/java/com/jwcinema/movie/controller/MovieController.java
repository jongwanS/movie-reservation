package com.jwcinema.movie.controller;

import com.jwcinema.movie.application.MovieService;
import com.jwcinema.movie.controller.dto.MovieRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            movieService.register(movieRegisterRequest);
            return ResponseEntity.ok().body("영화등록 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
