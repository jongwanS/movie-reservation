package com.jwcinema.screen.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jwcinema.common.InvalidParameterException;
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

    public void validate() {
        if(ObjectUtils.isEmpty(movieTitle)){
            throw new InvalidParameterException("movieId 는 필수값");
        }
        if(ObjectUtils.isEmpty(price)){
            throw new InvalidParameterException("price 는 필수값");
        }
        if(ObjectUtils.isEmpty(startAt)){
            throw new InvalidParameterException("startAt 는 필수값");
        }
    }
}
