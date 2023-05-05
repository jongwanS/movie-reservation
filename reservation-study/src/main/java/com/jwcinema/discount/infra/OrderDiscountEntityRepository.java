package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.OrderDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderDiscountEntityRepository extends JpaRepository<OrderDiscountEntity, Long> {

    Optional<OrderDiscountEntity> findByDiscountDateAndDayOfOrder(LocalDate discountDate, Integer dayOfOrder);
}
