package ru.practicum.ewm.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        log.error("404 NOT FOUND {} ", e.getMessage());
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.NOT_FOUND.name())
                .reason("Искомый объект не был найден")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException e) {
        log.error("409 CONFLICT {} ", e.getMessage());
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.CONFLICT.name())
                .reason("Попытка добавления уже существующих данных")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(ForbiddenException e) {
        log.error("409 FORBIDDEN {} ", e.getMessage());
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.FORBIDDEN.name())
                .reason("Некорректно составлен запрос")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(IncorrectParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectParamException(IncorrectParamException e) {
        log.error("400 BAD REQUEST {} ", e.getMessage());
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Некорректные параметры запроса")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
