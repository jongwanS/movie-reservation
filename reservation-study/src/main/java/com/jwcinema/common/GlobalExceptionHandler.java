package com.jwcinema.common;

import com.jwcinema.discount.domain.DuplicateOrderDiscountException;
import com.jwcinema.movie.controller.dto.MovieRegisterException;
import com.jwcinema.movie.domain.MovieAlreadyExistException;
import com.jwcinema.screen.domain.ScreenRegisterException;
import com.jwcinema.screen.domain.ScreenScheduleExistException;
import com.jwcinema.ticketing.domain.ScreenScheduleNotExistException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);

        final ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler({
            InvalidParameterException.class
    })
    protected ResponseEntity<?> InvalidParameterException(Exception e) {
        log.error("InvalidParameterException", e);
        ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({
            MovieRegisterException.class
    })
    protected ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        log.error("handleBadRequestException", e);
        ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({
            MovieAlreadyExistException.class
            , DuplicateOrderDiscountException.class
            , ScreenRegisterException.class
            , ScreenScheduleExistException.class
            , ScreenScheduleNotExistException.class
    })
    protected ResponseEntity<ErrorResponse> handleConditionFailException(Exception e) {
        log.error("handleBadRequestException", e);
        ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.PRECONDITION_FAILED.value())
                .message(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.PRECONDITION_FAILED);
    }
//    @ExceptionHandler({FileSystemException.class, RemoteException.class})

}