package ru.practicum.ewm.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleOtherException(Throwable e) {
        log.error("500 Internal Server Error {} ", e.getMessage());
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Internal Server Error")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerIncorrectParametersException(Exception e) {
        log.debug("400 BAD_REQUEST {}", e.getMessage(), e);
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.BAD_REQUEST.toString())
                .reason(e.getCause().getLocalizedMessage())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlerDataException(Exception e) {
        log.debug("409 CONFLICT {}", e.getMessage(), e);
        return ApiError.builder()
                .errors(Collections.singletonList(e.getMessage()))
                .status(HttpStatus.CONFLICT.toString())
                .reason(e.getCause().getLocalizedMessage())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
