package donatehub.domain.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import donatehub.domain.response.ExceptionResponse;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Qualifier("log")
    private final Logger logger;

    public GlobalExceptionHandler(@Qualifier("log") Logger logger) {
        this.logger = logger;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handler(AccessDeniedException ex) {
        logger.error("Access denied: \n{}, status: {}", ex.getMessage(), HttpStatus.FORBIDDEN);

        return new ResponseEntity<>("Siz ushbu resursga kirishga ruxsat etilmagan", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handler(BaseException e){
        logger.error("BaseException: \n{}, status: {}", e.getMessage(), e.getStatus());
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException e){
        Map<String, String> errors = e.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        logger.error("MethodArgumentNotValidException: \n{}, status: {}", errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("IllegalArgumentException: \n{}, status: {}", e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("HttpMessageNotReadableException: \n{}, status: {}", ex.getMessage(), HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handler(ExpiredJwtException e){
        logger.error("ExpiredJwtException: \n{} \nstatus: {}", e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(new ExceptionResponse("Token eskirgan"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handler(Exception e){
        logger.error("Exception: \n{} \nstatus: {}", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
