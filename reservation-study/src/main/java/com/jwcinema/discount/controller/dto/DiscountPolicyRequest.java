package com.jwcinema.discount.controller.dto;

import com.jwcinema.common.InvalidParameterException;
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

    @Builder.Default
    private String type="";
    private int rate;
    private long price;

    public void validate() {
        if(ObjectUtils.isEmpty(type)){
            throw new InvalidParameterException("type 필수");
        }
        if(!DiscountType.contains(type)){
            throw new InvalidParameterException("잘못된 할인 타입 type ::"+ type);
        }
        if(DiscountType.RATE.getValue().equals(type)){
            if(ObjectUtils.isEmpty(rate))
                throw new InvalidParameterException("rate 필수");
            if(rate <= 0 || rate >= 100){
                throw new InvalidParameterException("rate 잘못된 범위값");
            }
        }
        if(DiscountType.FIX.getValue().equals(type)){
            if(ObjectUtils.isEmpty(price) || price <= 0)
                throw new InvalidParameterException("price 필수");
        }
    }
}
