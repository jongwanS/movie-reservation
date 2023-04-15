package com.movie.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @PostMapping("/my/orders/{orderNo}/cancel")
    public ResponseEntity<?> orderDetai(){


        return ResponseEntity.ok().body(new Object());
    }
}
