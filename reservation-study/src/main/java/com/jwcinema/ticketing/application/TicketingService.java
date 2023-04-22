package com.jwcinema.ticketing.application;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.TicketingReserveRequest;
import com.jwcinema.ticketing.domain.*;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketingService {
    private final ScreenEntityRepository screenEntityRepository;
    private final MovieEntityRepository movieEntityRepository;
    private final TicketingEntityRepository ticketingEntityRepository;

    public Ticketing reserve(TicketingReserveRequest ticketingReserveRequest) throws Exception {
        ScreenEntity screenEntity = screenEntityRepository.findById(ticketingReserveRequest.getScreenId())
                .orElseThrow(() -> new RuntimeException(ticketingReserveRequest.getScreenId() + " 로 등록된 상영정보 없습니다"));
        Screen screen = screenEntity.toScreen();

        MovieEntity movieEntity = movieEntityRepository.findById(screen.getMovieId())
                .orElseThrow(() -> new RuntimeException("등록된 영화정보가 없습니다"));
        Movie movie = movieEntity.toMovie();

        Ticketing ticketing = Ticketing.builder()
                .ticketCount(ticketingReserveRequest.getTicketCount())
                .movie(movie)
                .screen(screen)
                .status(Status.COMPLETE)
                .build();
        ticketing.seatLimitValidte();

        ticketingEntityRepository.save(
                TicketingEntity.builder()
                        .ticketCount(ticketing.getTicketCount())
                        .status(ticketing.getStatus())
                        .build());
        return ticketing;
    }

    public TicketingEntity cancel(Long ticketingId) {
        TicketingEntity ticketingEntity = ticketingEntityRepository.findById(ticketingId)
                .orElseThrow(() -> new RuntimeException(ticketingId + " 로 등록된 티켓정보가 없습니다"));
        ticketingEntity.cancel();
        return ticketingEntity;
    }
}
