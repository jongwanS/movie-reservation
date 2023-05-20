package com.jwcinema.discount.controller;

import com.jwcinema.discount.application.DiscountService;
import com.jwcinema.discount.controller.dto.OrderDiscountRequest;
import com.jwcinema.discount.domain.OrderDiscount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/order/register")
    public ResponseEntity<?> order(
            @RequestBody OrderDiscountRequest orderDiscountRequest
    ) {
        orderDiscountRequest.validate();
        OrderDiscount discount = discountService.register(orderDiscountRequest);
        return ResponseEntity.ok().body(discount);

    }
}
