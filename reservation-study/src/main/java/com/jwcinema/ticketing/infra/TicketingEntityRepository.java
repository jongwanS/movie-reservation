package com.jwcinema.ticketing.infra;

import com.jwcinema.ticketing.domain.Ticketing;
import com.jwcinema.ticketing.domain.TicketingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketingEntityRepository extends CrudRepository<TicketingEntity, Long>,TicketingEntityRepositoryCustom {

}
