package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.OrderDiscount;
import com.jwcinema.discount.domain.OrderDiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderDiscountEntityRepository extends JpaRepository<OrderDiscountEntity, Long>, OrderDiscountEntityRepositoryCustom {

    Optional<OrderDiscountEntity> findByDiscountDateAndDayOfOrder(LocalDate localDate, Integer dayOfOrder);

    @Query("SELECT new com.jwcinema.discount.domain.OrderDiscount(od.id, od.policy) " +
            "FROM OrderDiscountEntity od " +
            "JOIN od.policy p " +
            "WHERE od.discountDate = :localDate " +
            "AND od.dayOfOrder = :dayOfOrder")
    Optional<OrderDiscount> findOrderDiscount(@Param("localDate") LocalDate localDate, @Param("dayOfOrder") Integer dayOfOrder);
}