package be.jimsa.iotproject.ws.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(
        name = "App Response",
        description = "This is a kind of 'ResponseDto'"
)
public class ResponseDto {

    @Schema(
            description = "What is the final result? It will be true if the process is finished without any faults, otherwise, it is false.",
            example = "true"
    )
    private boolean action;

    @Schema(
            description = "The time of response"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "The result of the response, including boolean, object, list, and so on"
    )
    private Object result;
}
