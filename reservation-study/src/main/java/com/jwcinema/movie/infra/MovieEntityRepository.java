package com.jwcinema.movie.infra;

import com.jwcinema.movie.domain.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieEntityRepository extends JpaRepository<MovieEntity, Long> {

     Optional<MovieEntity> findByTitle(String title);
}
