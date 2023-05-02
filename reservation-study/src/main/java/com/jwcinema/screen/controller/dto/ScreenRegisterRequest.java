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

    private String movieTitle;
    private Long price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(movieTitle)){
            throw new ScreenRegisterException("movieId 는 필수값");
        }
        if(ObjectUtils.isEmpty(price)){
            throw new ScreenRegisterException("price 는 필수값");
        }
        if(ObjectUtils.isEmpty(startAt)){
            throw new ScreenRegisterException("startAt 는 필수값");
        }
    }
}
