package com.jwcinema.order.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Reservation {
    Long seq;
    Long userId;
    Long movieSeq;
    Long movieTimetableSeq;
    Long paymentPrice;
    LocalDateTime insertDate;
}
