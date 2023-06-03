package com.jwcinema.discount.domain;

import com.jwcinema.common.InvalidParameterException;
import com.jwcinema.ticketing.domain.DiscountType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

@Getter
public class DiscountPolicy {

    private String type;
    private Integer rate;
    private Long price;

    private boolean isRateDiscount(String type) {
        return DiscountType.RATE.getValue().equals(type);
    }

    public long apply(long screenPrice) {
        if(isRateDiscount(this.getType())){
            return (screenPrice * getRate()/100);
        }
        validateDiscountPrice(screenPrice-price);
        return (screenPrice-price);
    }

    private long validateDiscountPrice(long price) {
        if(price <= 0){
            throw new RuntimeException("할인가가 더큼");
        }

        return price;
    }

    @Builder
    public DiscountPolicy(String type, Integer rate, Long price) {
        this.type = type;
        this.rate = rate;
        this.price = price;
        validate();
    }

    private void validate(){
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
