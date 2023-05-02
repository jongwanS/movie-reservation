package com.jwcinema.ticketing.application;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.*;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketingService {
    private final ScreenEntityRepository screenEntityRepository;
    private final TicketingEntityRepository ticketingEntityRepository;

    @Transactional
    public void reserve(TicketingRequest ticketingRequest){
        screenEntityRepository.findById(ticketingRequest.getScreenId())
                .orElseThrow(() -> new RuntimeException(ticketingRequest.getScreenId() + " 로 등록된 상영정보 없습니다"));

        ticketingEntityRepository.save(
                TicketingEntity.builder()
                        .ticketCount(ticketingRequest.getTicketCount())
                        .status(Status.COMPLETE)
                        .build());
    }

    @Transactional
    public void cancel(Long ticketingId) {
        TicketingEntity ticketingEntity = ticketingEntityRepository.findById(ticketingId)
                .orElseThrow(() -> new RuntimeException(ticketingId + " 로 등록된 티켓정보가 없습니다"));
        ticketingEntity.cancel();
    }
}
