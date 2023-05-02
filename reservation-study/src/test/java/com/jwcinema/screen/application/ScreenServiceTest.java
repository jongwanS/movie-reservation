package com.jwcinema.screen.application;

import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.controller.dto.ScreenRegisterRequest;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.domain.ScreenScheduleExistException;
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
                .movieTitle("리바운드")
                .price(3000L)
                .startAt(LocalDateTime.now().plusDays(1))
                .build();
        when(movieEntityRepository.findByTitle(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findByTitle(any());
    }

    @Test
    @DisplayName("상영정보 등록 - 실패(상영 시작시간, 종료시간에 겹치는 상영 예정영화가 존재함 -> ScreenScheduleExistException)")
    void register_fail_screenScheduleExist(){
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieTitle("리바운드")
                .price(3000L)
                .startAt(LocalDateTime.now().plusDays(1))
                .build();

        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title(screenRegisterRequest.getMovieTitle())
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now())
                .build();
        when(movieEntityRepository.findByTitle(screenRegisterRequest.getMovieTitle())).thenReturn(Optional.of(savedEntity));
        when(screenEntityRepository.findByStartAtAndEndAt(any(),any())).thenThrow(new ScreenScheduleExistException());


        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findByTitle(any());
    }

    @Test
    @DisplayName("상영정보 등록 - 실패(당일 등록된 영화는, 상영표에 당일날 등록될 수 없다.)")
    void register_fail_screen_registerNotAvailable(){
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieTitle("리바운드")
                .price(3000L)
                .startAt(LocalDateTime.of(2023, 4, 30, 14, 30))
                .build();

        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title(screenRegisterRequest.getMovieTitle())
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now())
                .build();
        when(movieEntityRepository.findByTitle(screenRegisterRequest.getMovieTitle())).thenReturn(Optional.of(savedEntity));


        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findByTitle(savedEntity.getTitle());
    }

    @Test
    @DisplayName("상영정보 등록 - 실패(상영 DB등록오류 -> RuntimeException)")
    void register_fail_screenSave(){
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieTitle("리바운드")
                .price(3000L)
                .startAt(LocalDateTime.now().plusDays(1))
                //LocalDateTime.of(2023, 4, 30, 14, 30)
                .build();
        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title(screenRegisterRequest.getMovieTitle())
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now().minusDays(1))
                .build();
        when(movieEntityRepository.findByTitle(savedEntity.getTitle())).thenReturn(Optional.of(savedEntity));

        when(screenEntityRepository.save(any(ScreenEntity.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> screenService.register(screenRegisterRequest));
        verify(movieEntityRepository, times(1)).findByTitle(savedEntity.getTitle());
        verify(screenEntityRepository, times(1)).save(any(ScreenEntity.class));
    }

    @Test
    @DisplayName("상영정보 등록 - 성공")
    void register_success() throws Exception {
        ScreenRegisterRequest screenRegisterRequest = ScreenRegisterRequest.builder()
                .movieTitle("리바운드")
                .price(3000L)
                .startAt(LocalDateTime.now().plusDays(1))
                .build();

        MovieEntity savedEntity = MovieEntity.builder()
                .id(1L)
                .title(screenRegisterRequest.getMovieTitle())
                .playtime(123)
                .description("하이")
                .insertDate(LocalDateTime.now().minusDays(1))
                .build();
        ScreenEntity screen = ScreenEntity.builder()
                .movieId(savedEntity.getId())
                .build();
        when(movieEntityRepository.findByTitle(savedEntity.getTitle())).thenReturn(Optional.of(savedEntity));
        when(screenEntityRepository.findByStartAtAndEndAt(any(),any())).thenReturn(Optional.empty());
        when(screenEntityRepository.save(any(ScreenEntity.class))).thenReturn(screen);

        screenService.register(screenRegisterRequest);
        verify(movieEntityRepository, times(1)).findByTitle(savedEntity.getTitle());
        verify(screenEntityRepository, times(1)).findByStartAtAndEndAt(any(),any());
        verify(screenEntityRepository, times(1)).save(any(ScreenEntity.class));
    }
}