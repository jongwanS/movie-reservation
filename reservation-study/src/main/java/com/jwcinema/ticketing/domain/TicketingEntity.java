package com.jwcinema.ticketing.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SequenceGenerator(
        name = "TICKETING_SEQ_GENERATOR",
        sequenceName = "TICKETING_SEQ",
        allocationSize = 1
)
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TICKETING")
public class TicketingEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKETING_SEQ_GENERATOR")
    private Long id;
    @Column(name = "ticket_id")
    private String ticketId;
    @Column(name = "ticket_count")
    private Integer ticketCount;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "payment_price")
    private double paymentPrice;
    @Column(name = "discount_price")
    private double discountPrice;

    public void cancel() {
        this.status = Status.CANCEL;
    }
}
