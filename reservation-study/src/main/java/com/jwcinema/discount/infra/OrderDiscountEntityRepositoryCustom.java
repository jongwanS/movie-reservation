package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.OrderDiscount;
import com.jwcinema.discount.domain.OrderDiscountEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderDiscountEntityRepositoryCustom {
//    @Query("SELECT new com.jwcinema.discount.domain.OrderDiscount(od.policy.type, od.policy.rate, od.policy.price) " +
//            "FROM OrderDiscountEntity od JOIN od.policy p " +
//            "WHERE od.discountDate = :localDate AND od.dayOfOrder = :dayOfOrder")
//    Optional<OrderDiscount> findOrderDiscount(@Param("localDate") LocalDate localDate, @Param("dayOfOrder") Integer dayOfOrder);

//    @Query("SELECT new com.jwcinema.discount.domain.OrderDiscount(od.policy.type, od.policy.rate, od.policy.price) " +
//            "FROM OrderDiscountEntity od " +
//            "WHERE od.discountDate = :localDate AND od.dayOfOrder = :dayOfOrder")
//    Optional<OrderDiscount> findOrderDiscount(@Param("localDate") LocalDate localDate, @Param("dayOfOrder") Integer dayOsfOrder);
//    @Query("SELECT new com.jwcinema.discount.domain.OrderDiscount(od.id, od.policy) " +
//            "FROM OrderDiscountEntity od " +
//            "JOIN od.policy p " +
//            "WHERE od.discountDate = :localDate " +
//            "AND od.dayOfOrder = :dayOfOrder")
//    Optional<OrderDiscount> findOrderDiscount(@Param("localDate") LocalDate localDate, @Param("dayOfOrder") Integer dayOfOrder);
}
