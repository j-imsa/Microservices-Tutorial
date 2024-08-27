package be.jimsa.iotproject.ws.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto {
    private boolean action;
    private LocalDateTime timestamp;
    private Object result;
}
