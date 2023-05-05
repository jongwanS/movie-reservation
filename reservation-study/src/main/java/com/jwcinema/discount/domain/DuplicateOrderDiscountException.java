package com.jwcinema.discount.domain;

public class DuplicateOrderDiscountException extends RuntimeException {
    public DuplicateOrderDiscountException(String str) {
        super(str);
    }
}
