package com.jwcinema.ticketing.infra;

import com.jwcinema.ticketing.domain.TicketingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketingEntityRepository extends JpaRepository<TicketingEntity, Long> {

}
