package com.jwcinema.screen.application;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.controller.dto.ScreenRegisterRequest;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreenServiceTest {

    @Mock
    private MovieEntityRepository movieEntityRepository;
    @Mock
    private ScreenEntityRepository screenEntityRepository;

    @InjectMocks
    private ScreenService screenService;

    @Test
    @DisplayName("상영정보 등록 - 실패(등록된 영화 미존재 -> RuntimeException)")
    void register_fail_movieNotExist(){
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(3000L)
                .startAt(LocalDateTime.of(2023, 4, 30, 14, 30))
                .endAt(LocalDateTime.of(2023, 4, 30, 16, 30))
                .build();
        when(movieEntityRepository.findById(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("상영정보 등록 - 실패(당일 등록된 영화는, 상영표에 당일날 등록될 수 없다.)")
    void register_fail_screen_registerNotAvailable(){
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(3000L)
                .startAt(LocalDateTime.of(2023, 4, 30, 14, 30))
                .endAt(LocalDateTime.of(2023, 4, 30, 16, 30))
                .build();

        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title("영화")
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now())
                .build();
        when(movieEntityRepository.findById(savedEntity.getId())).thenReturn(Optional.of(savedEntity));


        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findById(savedEntity.getId());
    }

    @Test
    @DisplayName("상영정보 등록 - 실패(상영 등록오류 -> RuntimeException)")
    void register_fail_screenSave(){
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(3000L)
                .startAt(LocalDateTime.of(2023, 4, 30, 14, 30))
                .endAt(LocalDateTime.of(2023, 4, 30, 16, 30))
                .build();
        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title("영화")
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now().minusDays(3))
                .build();
        when(movieEntityRepository.findById(savedEntity.getId())).thenReturn(Optional.of(savedEntity));

        when(screenEntityRepository.save(any(ScreenEntity.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findById(savedEntity.getId());
        verify(screenEntityRepository, times(1)).save(any(ScreenEntity.class));
    }

    @Test
    @DisplayName("상영정보 등록 - 성공")
    void register_success() throws Exception {
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(3000L)
                .startAt(LocalDateTime.now().plusDays(1))
                .endAt(LocalDateTime.now().plusDays(1).minusHours(1))
                .build();
        ScreenEntity screen = ScreenEntity.builder()
                .movieId(screenRegisterRequest.getMovieId())
                .build();

        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title("영화")
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now().minusDays(1))
                .build();
        when(movieEntityRepository.findById(savedEntity.getId())).thenReturn(Optional.of(savedEntity));

        when(screenEntityRepository.save(any(ScreenEntity.class))).thenReturn(screen);

        screenService.register(screenRegisterRequest);
        verify(movieEntityRepository, times(1)).findById(savedEntity.getId());
        verify(screenEntityRepository, times(1)).save(any(ScreenEntity.class));
    }
}