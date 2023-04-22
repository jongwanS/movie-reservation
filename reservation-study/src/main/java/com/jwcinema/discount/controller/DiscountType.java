package com.jwcinema.discount.controller;

import lombok.Getter;

@Getter
public enum DiscountType {
    RATE("RATE","정률"),
    FIX("FIX","정액");

    private String value;
    private String description;

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
