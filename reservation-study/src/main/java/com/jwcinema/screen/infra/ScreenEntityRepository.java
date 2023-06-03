package com.jwcinema.screen.infra;

import com.jwcinema.screen.domain.ScreenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ScreenEntityRepository extends JpaRepository<ScreenEntity, Long>, ScreenEntityRepositoryCustom {

    Optional<ScreenEntity> findByStartAtAndEndAt(LocalDateTime startAt, LocalDateTime endAt);

    //@Query("SELECT COUNT(*) FROM ScreenEntity sc WHERE sc.movieTitle = :movieTitle AND sc.startAt <= :startAt AND sc.endAt >= :endAt AND DATE_TRUNC(:startAt) = :startDate")
//    @Query(value = "SELECT COUNT(*) FROM ScreenEntity sc WHERE sc.movieTitle = :movieTitle " +
//            "AND sc.startAt <= :startAt AND sc.endAt >= :endAt " +
//            "AND YEAR(sc.startAt) = YEAR(:startDate) " +
//            "AND MONTH(sc.startAt) = MONTH(:startDate) " +
//            "AND DAY(sc.startAt) = DAY(:startDate)", nativeQuery = true)
    @Query(value = "select ta.num from\n" +
            "(\n" +
            "select ROW_NUMBER() OVER() as num, a.*\n" +
            "from\n" +
            "screen a\n" +
            "where  FORMATDATETIME(a.start_at,'yyyy-MM-dd')=:startDate\n" +
            ")ta\n" +
            "where  ta.start_at=:startAt\n" +
            "    and ta.end_at=:endAt\n" +
            "    and ta.movie_title = :movieTitle", nativeQuery = true)
    int findDayOfOrder(@Param("movieTitle") String movieTitle, @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt, @Param("startDate") LocalDate startDate);


}
