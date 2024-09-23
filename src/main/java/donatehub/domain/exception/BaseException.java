package donatehub.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus status;

    public BaseException(String message, HttpStatus statusCode) {
        super(message);
        this.status = statusCode;
    }
}
