package com.jwcinema.discount.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@SequenceGenerator(
        name = "PERIOD_DISCOUNT_SEQ_GENERATOR",
        sequenceName = "PERIOD_DISCOUNT_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PERIOD_DISCOUNT")
public class PeriodDiscountEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERIOD_DISCOUNT_SEQ_GENERATOR")
    private Long id;
    @Column(name = "start_at")
    private LocalDateTime startAt;
    @Column(name = "end_at")
    private LocalDateTime endAt;

}
