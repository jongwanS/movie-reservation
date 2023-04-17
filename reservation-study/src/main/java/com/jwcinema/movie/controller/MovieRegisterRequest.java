package com.jwcinema.movie.controller;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


@Getter
public class MovieRegisterRequest {
    private String title;
    private Integer playtime;
    private Optional<String> description;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(title)){
            throw new Exception();
        }
        if(ObjectUtils.isEmpty(playtime)){
            //오류
            throw new Exception();
        }
    }
}
