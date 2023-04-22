package com.jwcinema.ticketing.controller;

import com.jwcinema.screen.application.ScreenService;
import com.jwcinema.screen.controller.ScreenRegisterRequest;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.ticketing.application.TicketingService;
import com.jwcinema.ticketing.domain.Ticketing;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "ticketing")
@RequiredArgsConstructor
public class TicketingController {

    private final TicketingService ticketingService;

    @PostMapping("/reserve")
    public ResponseEntity<?> ticketingReserve(
            @RequestBody TicketingReserveRequest ticketingReserveRequest
    ) {
        try {
            ticketingReserveRequest.validate();
            Ticketing ticketing = ticketingService.reserve(ticketingReserveRequest);
            return ResponseEntity.ok().body(ticketing);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> ticketingCancel(
            @RequestBody Long ticketingId
    ) {
        try {
            if(ObjectUtils.isEmpty(ticketingId)){
                throw new Exception("예매정보가 없습니다.");
            }
            return ResponseEntity.ok().body(ticketingService.cancel(ticketingId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}