package be.jimsa.iotproject.config.exception.handler;

import be.jimsa.iotproject.config.exception.BadFormatRequestException;
import be.jimsa.iotproject.config.exception.InternalServiceException;
import be.jimsa.iotproject.config.exception.ResourceAlreadyExistException;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {
            BadFormatRequestException.class,
            ResourceAlreadyExistException.class
    })
    public ResponseEntity<ResponseDto> handleApp400Exceptions(RuntimeException ex, HttpServletRequest webRequest) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH, String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }

    @ExceptionHandler(value = {
            Exception.class,
            InternalServiceException.class
    })
    public ResponseEntity<ResponseDto> handleApp500Exceptions(RuntimeException ex, HttpServletRequest webRequest) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH, String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ResponseDto.builder()
                                .action(false)
                                .timestamp(LocalDateTime.now())
                                .result(hashMap)
                                .build()
                );
    }
}
