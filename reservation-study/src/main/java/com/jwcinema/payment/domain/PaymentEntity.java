package com.jwcinema.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SequenceGenerator(
        name = "PAYMENT_SEQ_GENERATOR",
        sequenceName = "PAYMENT_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PAYMENT")
public class PaymentEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_SEQ_GENERATOR")
    private Long id;
    @Column(name = "ticket_id")
    private String ticketId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "paid_price")
    private double paidPrice;
    @Column(name = "discount_price")
    private double discountPrice;
}
