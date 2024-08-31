package be.jimsa.iotdevice.ws.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseDto {
    private boolean action;
    private LocalDateTime timestamp;
    private Object result;
}
