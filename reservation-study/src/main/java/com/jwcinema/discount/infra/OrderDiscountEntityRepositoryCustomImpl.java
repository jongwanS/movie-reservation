package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.OrderDiscount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderDiscountEntityRepositoryCustomImpl implements OrderDiscountEntityRepositoryCustom{

//    private final JPAQueryFactory queryFactory;
//    @Override
//    @Query
//    public Optional<OrderDiscount> findOrderDiscount(LocalDate localDate, Integer dayOfOrder) {
//        return Optional.empty();
//    }
}
