package be.jimsa.iotproject.config.exception.handler;

import be.jimsa.iotproject.config.exception.BadFormatRequestException;
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

    @ExceptionHandler(value = {BadFormatRequestException.class, ResourceAlreadyExistException.class})
    public ResponseEntity<ResponseDto> handleAppExceptions(RuntimeException ex, HttpServletRequest webRequest) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH, String.format("%s %s", webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .action(false)
                        .timestamp(LocalDateTime.now())
                        .result(hashMap)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneralException(Exception ex, HttpServletRequest webRequest) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH, String.format("%s %s", webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.builder()
                        .action(false)
                        .timestamp(LocalDateTime.now())
                        .result(hashMap)
                        .build()
                );
    }
}
