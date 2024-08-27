package be.jimsa.iotproject.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NullObjectException extends RuntimeException {
    public NullObjectException(String message) {
        super(message);
    }
}
