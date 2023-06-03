package com.jwcinema.discount.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@SequenceGenerator(
        name = "ORDER_DISCOUNT_SEQ_GENERATOR",
        sequenceName = "ORDER_DISCOUNT_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDER_DISCOUNT")
public class OrderDiscountEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_DISCOUNT_SEQ_GENERATOR")
    private Long id;
    @Column(name = "day_of_order")
    private Integer dayOfOrder;
    @Column(name = "discount_date")
    private LocalDate discountDate;

    @OneToOne
    @JoinColumn(name = "id")
    private DiscountPolicyEntity policy;


    public Optional<OrderDiscount> toOrderDiscount(){
        return Optional.ofNullable(OrderDiscount.builder()
                .id(new DiscountId(dayOfOrder, discountDate))
                .policy(DiscountPolicy.builder()
                        .price(policy.getPrice())
                        .rate(policy.getRate())
                        .type(policy.getType())
                        .build())
                .build());
    }
}
