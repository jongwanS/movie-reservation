package com.jwcinema.screen.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class ScreenRegisterRequest {

    private Long movieId;
    private Long price;
    private Optional<Integer> seatLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(movieId)){
            throw new Exception("movieId 는 필수값");
        }
        if(ObjectUtils.isEmpty(price)){
            throw new Exception("price 는 필수값");
        }
        if(ObjectUtils.isEmpty(startAt)){
            throw new Exception("startAt 는 필수값");
        }
        if(ObjectUtils.isEmpty(endAt)){
            throw new Exception("endAt 는 필수값");
        }
    }
}
