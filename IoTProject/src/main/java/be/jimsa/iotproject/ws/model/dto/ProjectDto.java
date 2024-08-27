package be.jimsa.iotproject.ws.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {
    @JsonProperty("public_id")
    private String publicId;
    private String name;
    private String type;
}
