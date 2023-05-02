package com.jwcinema.movie.application;

import com.jwcinema.movie.controller.dto.MovieRegisterRequest;
import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieEntityRepository movieEntityRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("영화 등록 - 실패(DB저장실패)")
    void register_fail() {
        //given
        MovieRegisterRequest movie = MovieRegisterRequest.builder()
                .title("영화제목")
                .playtime(100)
                .description("영화설명")
                .build();
        when(movieEntityRepository.save(any(MovieEntity.class))).thenThrow(new RuntimeException());

        // when, then
        assertThrows(RuntimeException.class, () -> movieService.register(movie));
        verify(movieEntityRepository, times(1)).save(any(MovieEntity.class));
    }

    @DisplayName("영화를 등록 - 성공")
    @Test
    void register_success(){
        //given
        MovieRegisterRequest movie = MovieRegisterRequest.builder()
                .title("영화제목")
                .playtime(100)
                .description("영화설명")
                .build();
        // when
        MovieEntity savedEntity = MovieEntity.builder()
                .title(movie.getTitle())
                .playtime(movie.getPlaytime())
                .description(movie.getDescription())
                .insertDate(LocalDateTime.now())
                .build();

        when(movieEntityRepository.save(any(MovieEntity.class))).thenReturn(savedEntity);
        movieService.register(movie);

        // then
        verify(movieEntityRepository, times(1)).save(any(MovieEntity.class));
    }
}