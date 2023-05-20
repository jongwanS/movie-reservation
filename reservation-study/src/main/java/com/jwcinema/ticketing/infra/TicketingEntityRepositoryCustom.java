package com.jwcinema.ticketing.infra;

import com.jwcinema.ticketing.domain.Ticketing;

import java.util.Optional;

public interface TicketingEntityRepositoryCustom {
    Optional<Ticketing> selectTickingById(String id);
}
