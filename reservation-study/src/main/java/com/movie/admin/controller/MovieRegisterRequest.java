package com.movie.admin.controller;

import lombok.Getter;
import org.springframework.util.ObjectUtils;


@Getter
public class MovieRegisterRequest {
    private String title;
    private String director;
    private String actor;
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
