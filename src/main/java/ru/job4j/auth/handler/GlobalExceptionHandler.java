package ru.job4j.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = AccessDeniedException.class)
    public void handleAccessDeniedException(Exception ex,
                                            HttpServletResponse res,
                                            HttpServletRequest req) throws IOException {
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(
                Map.of(
                        "message", ex.getMessage(),
                        "status", res.getStatus(),
                        "error", HttpStatus.FORBIDDEN.getReasonPhrase(),
                        "timestamp", LocalDateTime.now(),
                        "path", req.getRequestURI()
                )
        ));
        log.error(ex.getMessage(), ex);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public void handleUniqConstraintViolation(DataIntegrityViolationException ex,
                                              HttpServletResponse res,
                                              HttpServletRequest req) throws IOException {
        if (ex.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException")
                && ((SQLException) ex.getMostSpecificCause()).getSQLState().equals("23505")) {
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setContentType("application/json");
            res.getWriter().write(objectMapper.writeValueAsString(
                    Map.of(
                            "message", ex.getMostSpecificCause().getMessage(),
                            "status", res.getStatus(),
                            "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "timestamp", LocalDateTime.now(),
                            "path", req.getRequestURI()
                    )
            ));
            log.error(ex.getMessage(), ex);
        } else {
            throw ex;
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getFieldErrors().stream()
                .map(f -> Map.of(f.getField(), String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())))
                .collect(Collectors.toList()));
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(ex.getConstraintViolations().stream()
                .map(cv -> Map.of(
                        cv.getPropertyPath().toString().split("\\.")[1],
                        String.format("%s. Actual value: %s", cv.getMessage(), cv.getInvalidValue())))
                .collect(Collectors.toList()));
    }
}
