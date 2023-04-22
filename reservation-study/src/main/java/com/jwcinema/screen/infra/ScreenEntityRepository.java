package com.jwcinema.screen.infra;

import com.jwcinema.screen.domain.ScreenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenEntityRepository extends JpaRepository<ScreenEntity, Long> {

}
