package com.jwcinema.discount.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jwcinema.common.InvalidParameterException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDiscountRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Integer dayOfOrder;
    private DiscountPolicyRequest policy;

    public void validate(){
        if(ObjectUtils.isEmpty(date)){
            throw new InvalidParameterException("date는 필수값입니다.");
        }
        if(date.isBefore(LocalDate.now())){
            throw new InvalidParameterException("date는 과거로 등록할 수 없습니다.");
        }
        if(ObjectUtils.isEmpty(dayOfOrder)){
            throw new InvalidParameterException("dayOfOrder 값은 필수잆니다.");
        }
        if(dayOfOrder <= 0){
            throw new InvalidParameterException("dayOfOrder 값은 0 또는 음수가 될 수 없습니다.");
        }
        if(ObjectUtils.isEmpty(policy)){
            throw new InvalidParameterException("discount 값은 필수값입니다.");
        }
        policy.validate();
    }
}
