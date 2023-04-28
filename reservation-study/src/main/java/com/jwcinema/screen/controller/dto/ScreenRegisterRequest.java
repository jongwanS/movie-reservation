package com.jwcinema.screen.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenRegisterRequest {

    private Long movieId;
    private Long price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(movieId)){
            throw new IllegalArgumentException("movieId 는 필수값");
        }
        if(ObjectUtils.isEmpty(price)){
            throw new IllegalArgumentException("price 는 필수값");
        }
        if(ObjectUtils.isEmpty(startAt)){
            throw new IllegalArgumentException("startAt 는 필수값");
        }
        if(ObjectUtils.isEmpty(endAt)){
            throw new IllegalArgumentException("endAt 는 필수값");
        }
    }
}
