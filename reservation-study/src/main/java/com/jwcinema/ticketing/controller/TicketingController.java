package com.jwcinema.ticketing.controller;

import com.jwcinema.ticketing.application.TicketingService;
import com.jwcinema.ticketing.controller.dto.TicketingCancelRequest;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.Ticketing;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody TicketingRequest ticketingRequest
    ) {
        ticketingRequest.validate();
        Ticketing ticketing = ticketingService.reserve(ticketingRequest);
        return ResponseEntity.ok().body(ticketing);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> ticketingCancel(
            @RequestBody TicketingCancelRequest ticketingCancelRequest
    ) {
        try {
            ticketingCancelRequest.validate();
            ticketingService.cancel(ticketingCancelRequest.getTicketingId());
            return ResponseEntity.ok().body("티케팅 취소 성공");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}