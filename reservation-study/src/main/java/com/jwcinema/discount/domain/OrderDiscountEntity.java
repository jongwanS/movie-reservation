package com.jwcinema.discount.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @Column(name = "date")
    private LocalDate date;
}
