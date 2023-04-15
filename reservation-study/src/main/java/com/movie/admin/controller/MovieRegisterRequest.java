package com.movie.admin.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
public class MovieRegisterRequest {
    private String title;
    private String director;
    private String cast;
    private String description;
    private Long price;

    public void validate() {
        if(ObjectUtils.isEmpty(title)){
            //오류
        }
        if(ObjectUtils.isEmpty(director)){
            //오류
        }
    }
}
