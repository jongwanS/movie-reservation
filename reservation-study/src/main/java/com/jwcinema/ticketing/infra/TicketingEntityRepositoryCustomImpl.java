package com.jwcinema.ticketing.infra;

import com.jwcinema.ticketing.domain.Ticketing;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TicketingEntityRepositoryCustomImpl implements TicketingEntityRepositoryCustom{

    @Override
    public Optional<Ticketing> selectTickingById(String id) {
        return Optional.empty();
    }
}
