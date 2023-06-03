package com.jwcinema.movie.domain;

import com.jwcinema.movie.controller.dto.MovieRegisterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class MovieTest {

    @Test
    @DisplayName("영화 정상등록 된다.")
    public void testMovieValidation_WithValidInputs_ShouldNotThrowException() {
        // Arrange
        String title = "Movie Title";
        Integer playtime = 120;
        String description = "Movie Description";

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            Movie movie = Movie.builder()
                    .title(title)
                    .playtime(playtime)
                    .description(description)
                    .build();
        });
    }

    @Test
    @DisplayName("영화 제목이 없어 오류가 발생한다.")
    public void testMovieValidation_WithEmptyTitle_ShouldThrowMovieRegisterException() {
        // Arrange
        String title = "";
        Integer playtime = 120;
        String description = "Movie Description";

        // Act & Assert
        Assertions.assertThrows(MovieRegisterException.class, () -> {
            Movie movie = Movie.builder()
                    .title(title)
                    .playtime(playtime)
                    .description(description)
                    .build();
        });
    }

    @Test
    @DisplayName("영화 상영시간이 없어 오류가 발생한다.")
    public void testMovieValidation_WithNullPlaytime_ShouldThrowMovieRegisterException() {
        // Arrange
        String title = "Movie Title";
        Integer playtime = null;
        String description = "Movie Description";

        // Act & Assert
        Assertions.assertThrows(MovieRegisterException.class, () -> {
            Movie movie = Movie.builder()
                    .title(title)
                    .playtime(playtime)
                    .description(description)
                    .build();
        });
    }
}