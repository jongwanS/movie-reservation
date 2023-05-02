package com.jwcinema.discount.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@Getter
public class OrderDiscountRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Integer dayOfOrder;
    private DiscountPolicyRequest discount;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(date)){
            throw new Exception("date는 필수값입니다.");
        }
        if(date.isBefore(LocalDate.now())){
            throw new Exception("date는 과거로 등록할 수 없습니다.");
        }
        if(ObjectUtils.isEmpty(dayOfOrder)){
            throw new Exception("dayOfOrder 값은 필수잆니다.");
        }
        if(dayOfOrder <= 0){
            throw new Exception("dayOfOrder 값은 0 또는 음수가 될 수 없습니다.");
        }
        if(ObjectUtils.isEmpty(discount)){
            throw new Exception("discount 값은 필수값입니다.");
        }
    }
}
