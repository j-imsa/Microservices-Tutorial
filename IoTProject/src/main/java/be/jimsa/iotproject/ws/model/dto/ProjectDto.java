package be.jimsa.iotproject.ws.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectDto {
    @JsonProperty("public_id")
    private String publicId;
    private String name;
    private String type;
}
