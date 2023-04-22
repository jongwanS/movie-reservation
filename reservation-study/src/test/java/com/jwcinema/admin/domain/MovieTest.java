package com.jwcinema.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MovieTest {

    @Test
    @DisplayName("상영시작 시간은 등록일보다 최소 24시간 이후이어야 한다.(24시간이전)")
    public void movieRegisterNotAvailable(){
        //given

//        Movie movie = Movie.builder()
//                .title("리바운드")
//                .director("장항준")
//                .actor("안재홍...")
//                .description("재미따")
//                .price(9000L)
//                .insertDate(LocalDateTime.now())
//                .build();
//
//
//        Assertions.assertFalse(
//                movie.registerAvailable());
    }

    @Test
    @DisplayName("상영시작 시간은 등록일보다 최소 24시간 이후이어야 한다.")
    public void movieRegisterAvailable(){
        //given

//        Movie movie = Movie.builder()
//                .title("리바운드")
//                .director("장항준")
//                .actor("안재홍...")
//                .description("재미따")
//                .price(9000L)
//                .insertDate(LocalDateTime.now().minusDays(2))
//                .build();
//
//
//        if(!movie.registerAvailable()){
//            Assertions.fail();
//        }

//        MovieTimeTable movieTimeTable = MovieTimeTable.builder()
//                .movieSeq(movie.getSeq())
//                .startDate(LocalDateTime.now().plusDays(1))
//                .endDate(LocalDateTime.now().plusDays(1).minusHours(2))
//                .build();
//
//        Assertions.assertTrue(movieTimeTable.getMovieSeq() != null);

    }
}