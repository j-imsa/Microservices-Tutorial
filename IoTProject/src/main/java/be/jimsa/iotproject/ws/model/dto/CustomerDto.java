package be.jimsa.iotproject.ws.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerDto {
    @JsonProperty("public_id")
    private String publicId;
    private String name;
    private String email;
    @JsonProperty("mobile_number")
    private String mobileNumber;
}
