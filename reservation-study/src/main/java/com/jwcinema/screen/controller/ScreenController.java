package com.jwcinema.screen.controller;

import com.jwcinema.screen.application.ScreenService;
import com.jwcinema.screen.controller.dto.ScreenRegisterRequest;
import com.jwcinema.screen.domain.ScreenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "screen")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping("/register")
    public ResponseEntity<?> screenRegister(
            @RequestBody ScreenRegisterRequest screenRegisterRequest
    ) {
        try {
            screenRegisterRequest.validate();
            screenService.register(screenRegisterRequest);
            return ResponseEntity.ok().body("상영등록 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}