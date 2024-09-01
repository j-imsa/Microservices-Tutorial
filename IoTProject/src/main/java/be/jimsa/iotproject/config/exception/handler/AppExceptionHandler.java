package be.jimsa.iotproject.config.exception.handler;

import be.jimsa.iotproject.config.exception.*;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleMethodArgumentNotValid : {}", ex.getMessage());
        Map<String, String> hashMap = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        errors.forEach(error ->
                hashMap.put((
                                (FieldError) error).getField(),
                        error.getDefaultMessage()
                )
        );
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
            BadFormatRequestException.class,
            ResourceAlreadyExistException.class
    })
    public ResponseEntity<ResponseDto> handleApp4xxExceptions(RuntimeException ex, HttpServletRequest webRequest) {
        log.error("handleApp4xxExceptions : {}", ex.getMessage());
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
            NotFoundResourceException.class
    })
    public ResponseEntity<ResponseDto> handleApp404Exceptions(NotFoundResourceException ex, HttpServletRequest webRequest) {
        log.error("handleApp404Exceptions : {}", ex.getMessage());
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(ProjectConstants.EXCEPTION_MESSAGE, ex.getMessage());
        hashMap.put(ProjectConstants.EXCEPTION_PATH,
                String.format(ProjectConstants.EXCEPTION_REGEX, webRequest.getMethod(), webRequest.getRequestURI()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
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
            NullObjectException.class,
            InternalServiceException.class
    })
    public ResponseEntity<ResponseDto> handleApp5xxExceptions(Exception ex, HttpServletRequest webRequest) {
        log.error("handleApp5xxExceptions : {}", ex.getMessage());
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
