package uz.mydonation.domain.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.mydonation.domain.response.ExceptionRes;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handler(AccessDeniedException ex) {
        return new ResponseEntity<>("Siz ushbu resursga kirishga ruxsat etilmagan", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionRes> handler(BaseException e){
        return new ResponseEntity<>(new ExceptionRes(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException e){
        return new ResponseEntity<>(
                e.getFieldErrors().stream().collect(
                        Collectors.toMap(
                                FieldError::getField,
                                DefaultMessageSourceResolvable::getDefaultMessage
                        )
                ),
                e.getStatusCode()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionRes> handler(Exception e){
        return new ResponseEntity<>(new ExceptionRes(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
