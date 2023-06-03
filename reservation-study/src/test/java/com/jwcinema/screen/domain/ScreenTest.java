package com.jwcinema.screen.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScreenTest {

    @Test
    @DisplayName("상영이 정상적으로 등록된다.")
    public void testCreateScreen_WithValidInputs_ShouldReturnScreenObject() {
        // Arrange
        String movieTitle = "Movie Title";
        LocalDateTime startAt = LocalDateTime.now().plusHours(1);
        Long price = 10000L;
        Integer playTime = 120;
        LocalDateTime movieInsertDate = LocalDateTime.now().minusDays(1);

        // Act
        Screen screen = Screen.createScreen(movieTitle, startAt, price, playTime, movieInsertDate);

        // Assert
        Assertions.assertNotNull(screen);
        Assertions.assertEquals(movieTitle, screen.getMovieTitle());
        Assertions.assertEquals(startAt, screen.getStartAt());
        Assertions.assertEquals(startAt.plusMinutes(playTime), screen.getEndAt());
        Assertions.assertEquals(price, screen.getPrice());
    }

    @Test
    @DisplayName("상영 시작 시간을 과거로 등록 할 수 없습니다.")
    public void testCreateScreen_WithPastStartAtTime_ShouldThrowScreenRegisterException() {
        // Arrange
        String movieTitle = "Movie Title";
        LocalDateTime startAt = LocalDateTime.now().minusHours(1);
        Long price = 10000L;
        Integer playTime = 120;
        LocalDateTime movieInsertDate = LocalDateTime.now().minusDays(1);

        // Act & Assert
        Assertions.assertThrows(ScreenRegisterException.class, () -> {
            Screen.createScreen(movieTitle, startAt, price, playTime, movieInsertDate);
        });
    }

    @Test
    @DisplayName("당일 등록한 영화는, 상영시간표에 등록할 수 없습니다.")
    public void testCreateScreen_WithSameDayMovieInsertDate_ShouldThrowScreenRegisterException() {
        // Arrange
        String movieTitle = "Movie Title";
        LocalDateTime startAt = LocalDateTime.now().plusHours(1);
        Long price = 10000L;
        Integer playTime = 120;
        LocalDateTime movieInsertDate = LocalDateTime.now();

        // Act & Assert
        Assertions.assertThrows(ScreenRegisterException.class, () -> {
            Screen.createScreen(movieTitle, startAt, price, playTime, movieInsertDate);
        });
    }
}