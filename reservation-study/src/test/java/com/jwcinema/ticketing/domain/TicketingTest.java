//package com.jwcinema.ticketing.domain;
//
//import com.jwcinema.common.InvalidParameterException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TicketingTest {
//
//    @Test
//    @DisplayName("티케팅 정상적으로 된다.")
//    public void testCreateTicket_WithValidParameters_ShouldCreateTicket() {
//        // Arrange
//        String phoneNumber = "01012345678";
//        int ticketCount = 2;
//        Long price = 10000L;
//        LocalDateTime startAt = LocalDateTime.now().plusHours(1);
//        LocalDateTime endAt = LocalDateTime.now().plusHours(2);
//        String movieTitle = "Avengers: Endgame";
//
//        // Act
//        Ticketing ticketing = Ticketing.createTicket(phoneNumber, ticketCount, price, startAt, endAt, movieTitle);
//
//        // Assert
//        Assertions.assertNotNull(ticketing);
//        Assertions.assertNotNull(ticketing.getId());
//        Assertions.assertEquals(ticketCount, ticketing.getTicketCount());
//    }
//
//    @Test
//    @DisplayName("티케팅 티켓갯수가 0개이므로 실패")
//    public void testCreateTicket_WithInvalidParameters_ShouldThrowInvalidParameterException() {
//        // Arrange
//        String phoneNumber = "01012345678";
//        int ticketCount = 0; // Invalid ticket count
//        Long price = 10000L;
//        LocalDateTime startAt = LocalDateTime.now().plusHours(1);
//        LocalDateTime endAt = LocalDateTime.now().plusHours(2);
//        String movieTitle = "히히";
//
//        // Act & Assert
//        Assertions.assertThrows(InvalidParameterException.class, () -> {
//            Ticketing.createTicket(phoneNumber, ticketCount, price, startAt, endAt, movieTitle);
//        });
//    }
//
//    @Test
//    @DisplayName("영화명이 없으므로 실패")
//    public void testCreateTicket_noMovieTitleInvalidParameters_ShouldThrowInvalidParameterException() {
//        // Arrange
//        String phoneNumber = "01012345678";
//        int ticketCount = 1; // Invalid ticket count
//        Long price = 10000L;
//        LocalDateTime startAt = LocalDateTime.now().plusHours(1);
//        LocalDateTime endAt = LocalDateTime.now().plusHours(2);
//        String movieTitle = "";
//
//        // Act & Assert
//        Assertions.assertThrows(InvalidParameterException.class, () -> {
//            Ticketing.createTicket(phoneNumber, ticketCount, price, startAt, endAt, movieTitle);
//        });
//    }
//}