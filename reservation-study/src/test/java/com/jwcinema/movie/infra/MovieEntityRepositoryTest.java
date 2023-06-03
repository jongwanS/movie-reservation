package com.jwcinema.movie.infra;

import com.jwcinema.movie.domain.MovieEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieEntityRepositoryTest {

    @Autowired
    private MovieEntityRepository movieEntityRepository;

    @Test
    @DisplayName("영화명으로 영화조회 - 존재하는 영화")
    public void findByTitle_ExistingTitle_ReturnsOptionalWithMovieEntity() {
        // Given
        MovieEntity movie = MovieEntity.builder()
                .title("리바운드")
                .build();
        movieEntityRepository.save(movie);

        // When
        Optional<MovieEntity> foundMovie = movieEntityRepository.findByTitle(movie.getTitle());

        // Then
        assertTrue(foundMovie.isPresent());
        assertEquals(movie.getTitle(), foundMovie.get().getTitle());
    }

    @Test
    @DisplayName("영화명으로 영화조회 - 존재하지 않는 영화")
    public void findByTitle_NonExistingTitle_ReturnsEmptyOptional() {
        // Given
        String title = "Non-existing Movie";

        // When
        Optional<MovieEntity> foundMovie = movieEntityRepository.findByTitle(title);

        // Then
        assertFalse(foundMovie.isPresent());
    }
}