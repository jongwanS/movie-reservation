package com.jwcinema.discount.controller.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
@Deprecated
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
        if(LocalDateTime.now().isAfter(startAt)){
            throw new Exception("startAt 정보가 과거입니다.");
        }
        if(ObjectUtils.isEmpty(endAt)){
            throw new Exception("endAt 정보는 필수");
        }

        if(LocalDateTime.now().isAfter(endAt)){
            throw new Exception("endAt 정보가 과거입니다.");
        }

        if(startAt.isAfter(endAt)){
            throw new Exception("startAt이 endAt보다 클수 없습니다.");
        }

        if(ObjectUtils.isEmpty(discount)) {
            throw new Exception("discount 정보는 필수");
        }
    }
}
