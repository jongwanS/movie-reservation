package com.jwcinema.screen.infra;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.domain.ScreenEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScreenEntityRepositoryTest {

    @Autowired
    private ScreenEntityRepository screenEntityRepository;

    @Autowired
    private MovieEntityRepository movieEntityRepository;

    @Test
    @DisplayName("상영등록시, 영화 시작시간 종료시간에 영화가 존재한다.")
    public void findByStartAtAndEndAt_ExistingScreen_ReturnsOptionalWithScreenEntity() {
        // Given
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(2);
        ScreenEntity screen = ScreenEntity.builder()
                .startAt(startAt)
                .endAt(endAt)
                .build();
        ScreenEntity screenEntity = screenEntityRepository.save(screen);
        List<ScreenEntity> as = screenEntityRepository.findAll();
        // When
        Optional<ScreenEntity> foundScreen = screenEntityRepository.findByStartAtAndEndAt(screenEntity.getStartAt(), screenEntity.getEndAt());

        // Then
        assertTrue(foundScreen.isPresent());
        assertEquals(screen.getStartAt(), foundScreen.get().getStartAt());
        assertEquals(screen.getEndAt(), foundScreen.get().getEndAt());
    }

    @Test
    @DisplayName("상영등록시, 영화 시작시간 종료시간에 영화가 존재하지않는다.")
    public void findByStartAtAndEndAt_NonExistingScreen_ReturnsEmptyOptional() {
        // Given
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.plusHours(2);

        // When
        Optional<ScreenEntity> foundScreen = screenEntityRepository.findByStartAtAndEndAt(startAt, endAt);

        // Then
        assertFalse(foundScreen.isPresent());
    }

    @Test
    @DisplayName("오늘 일자 영화의 순번을 구한다.")
    public void findDayOfOrder_ValidInput_ReturnsOrderCount() {
        // Given
        String movieTitle = "리바운드";
        LocalDateTime startAt = LocalDateTime.now().minusHours(1);
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        LocalDate startDate = LocalDate.now();
        ScreenEntity screen = ScreenEntity.builder()
                .movieTitle("리바운드")
                .startAt(startAt)
                .endAt(endAt)
                .build();
        ScreenEntity screenEntity = screenEntityRepository.save(screen);
        List<ScreenEntity> as = screenEntityRepository.findAll();
        // When
        int orderCount = screenEntityRepository.findDayOfOrder(movieTitle, startAt, endAt, startDate);

        // Then
        assertEquals(0, orderCount); // Assuming no orders are present for the given input
    }

    @Test
    @DisplayName("오늘 일자 영화의 순번을 구한다. 1번째")
    public void findDayOfOrder_exist_ReturnsOrderCount() {
        // Given
        String movieTitle = "리바운드";
        LocalDateTime startAt = LocalDateTime.now().minusHours(1);
        LocalDateTime endAt = LocalDateTime.now().plusHours(1);
        LocalDate startDate = LocalDate.now();

        ScreenEntity screen = ScreenEntity.builder()
                .movieTitle("리바운드")
                .startAt(startAt)
                .endAt(endAt)
                .build();
        screenEntityRepository.save(screen);

        // When
        int orderCount = screenEntityRepository.findDayOfOrder(movieTitle, startAt, endAt, startDate);

        // Then
        assertEquals(1, orderCount); // Assuming no orders are present for the given input
    }
}