package com.jwcinema.ticketing.domain;

import lombok.Getter;

@Getter
public enum DiscountType {
    RATE("RATE","정률"),
    FIX("FIX","정액");

    private final String value;
    private final String description;

    DiscountType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static boolean contains(String type){
        for (DiscountType discountType : DiscountType.values()){
            if(discountType.getValue().equals(type)){
                return true;
            }
        }
        return false;
    }
}
