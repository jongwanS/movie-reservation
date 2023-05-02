package com.jwcinema.screen.infra;

import com.jwcinema.screen.domain.ScreenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ScreenEntityRepository extends JpaRepository<ScreenEntity, Long> {

    Optional<ScreenEntity> findByStartAtAndEndAt(LocalDateTime startAt, LocalDateTime endAt);
}
