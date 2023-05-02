package com.jwcinema.movie.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRegisterRequest {
    private String title;
    private Integer playtime;
    private String description;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(title)){
            throw new MovieRegisterException("title 은 필수값");
        }
        if(ObjectUtils.isEmpty(playtime)){
            throw new MovieRegisterException("playtime 필수값");
        }
    }
}
