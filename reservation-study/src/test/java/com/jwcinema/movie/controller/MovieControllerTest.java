package com.jwcinema.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwcinema.movie.application.MovieService;
import com.jwcinema.movie.controller.dto.MovieRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("영화 등록 - 실패(title, null)")
    @Test
    void register_fail_missingTitle() throws Exception {
        MovieRegisterRequest movieRegisterRequest = MovieRegisterRequest.builder()
                .description("영화설명")
                .playtime(123)
                .build();

        mvc.perform(post("/movie/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(movieRegisterRequest))
                )
                .andExpect(status().isBadRequest());

    }
    @DisplayName("영화 등록 - 실패(playTime, null)")
    @Test
    void register_fail_missingPlayTime() throws Exception {
        MovieRegisterRequest movieRegisterRequest = MovieRegisterRequest.builder()
                .title("영화제목")
                .description("영화설명")
                .build();

        mvc.perform(post("/movie/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(movieRegisterRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("영화 등록시, movieService 의 register 메서드를 호출한다.")
    @Test
    void register_service_call_verification() throws Exception {
        MovieRegisterRequest movieRegisterRequest = MovieRegisterRequest.builder()
                .title("영화제목")
                .playtime(100)
                .description("영화설명")
                .build();

        mvc.perform(post("/movie/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(movieRegisterRequest))
                )
                .andExpect(status().isOk());
        verify(movieService, times(1)).register(any());
    }

    @DisplayName("영화 등록 - 성공")
    @Test
    void register_success() throws Exception {
        MovieRegisterRequest movieRegisterRequest = MovieRegisterRequest.builder()
                .title("영화제목")
                .playtime(100)
                .description("영화설명")
                .build();

        mvc.perform(post("/movie/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(movieRegisterRequest))
                )
                .andExpect(status().isOk());
    }

    private <T> String toJson(T body) throws JsonProcessingException {
        return objectMapper.writeValueAsString(body);
    }
}