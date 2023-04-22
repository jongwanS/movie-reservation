package com.jwcinema.discount.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
public class PeriodDiscountRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;
    private DiscountPolicyRequest discount;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(startAt)){
            throw new Exception("startAt 정보는 필수");
        }
        if(ObjectUtils.isEmpty(endAt)){
            throw new Exception("endAt 정보는 필수");
        }
        if(ObjectUtils.isEmpty(discount)) {
            throw new Exception("discount 정보는 필수");
        }
    }
}
