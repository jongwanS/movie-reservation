package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.PeriodDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodDiscountEntityRepository extends JpaRepository<PeriodDiscountEntity, Long> {

}
