package com.jwcinema.discount.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicyRequest {
    private String type;
    private Integer rate;
    private Long price;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(type)){
            throw new Exception("type 필수");
        }
        if(!DiscountType.contains(type)){
            throw new Exception("잘못된 할인 타입 type ::"+ type);
        }
        if(DiscountType.RATE.getValue().equals(type)){
            if(ObjectUtils.isEmpty(rate))
                throw new Exception("rate 필수");
            if(rate <= 0 || rate >= 100){
                throw new Exception("rate 잘못된 범위값");
            }
        }
        if(DiscountType.FIX.getValue().equals(type)){
            if(ObjectUtils.isEmpty(price))
                throw new Exception("price 필수");
        }
    }
}
