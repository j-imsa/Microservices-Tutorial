package be.jimsa.iotdevice.config.exception.handler;

import be.jimsa.iotdevice.config.exception.AppException;
import be.jimsa.iotdevice.ws.model.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ResponseDto> handleAppException(AppException e, HttpServletRequest request) {
        return null;
    }
}
