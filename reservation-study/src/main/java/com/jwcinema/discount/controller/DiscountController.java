package com.jwcinema.discount.controller;

import com.jwcinema.discount.application.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/period/register")
    public ResponseEntity<?> period(
            @RequestBody PeriodDiscountRequest periodDiscountRequest
    ) {
        try {
            periodDiscountRequest.validate();
            discountService.register(periodDiscountRequest);
            return ResponseEntity.ok().body("기간할인 등록 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/order/register")
    public ResponseEntity<?> order(
            @RequestBody OrderDiscountRequest orderDiscountRequest
    ) {
        try {
            orderDiscountRequest.validate();
            discountService.register(orderDiscountRequest);
            return ResponseEntity.ok().body("순번할인 등록 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
