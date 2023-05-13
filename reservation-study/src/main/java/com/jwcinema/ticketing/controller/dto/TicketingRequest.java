package com.jwcinema.ticketing.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jwcinema.common.ErrorCode;
import com.jwcinema.common.InvalidParameterException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketingRequest {

    private int ticketCount;
    @Builder.Default
    private String movieTitle="";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAt;
    @Builder.Default
    private String phoneNumber="";

    public void validate() {

        if(ObjectUtils.isEmpty(movieTitle)){
            throw new InvalidParameterException(ErrorCode.MOVIE_TITLE_REQUIRED.getMessage());
        }
        if(ObjectUtils.isEmpty(ticketCount) || ticketCount < 1){
            throw new InvalidParameterException(ErrorCode.TICKET_COUNT_REQUIRED.getMessage());
        }
        if(ObjectUtils.isEmpty(phoneNumber)){
            throw new InvalidParameterException(ErrorCode.MOBILE_NO_REQUIRED.getMessage());
        }
        if(ObjectUtils.isEmpty(startAt)){
            throw new InvalidParameterException(ErrorCode.MOVIE_START_TIME_REQUIRED.getMessage());
        }
        if(ObjectUtils.isEmpty(endAt)){
            throw new InvalidParameterException(ErrorCode.MOVIE_END_TIME_REQUIRED.getMessage());
        }
    }
}
