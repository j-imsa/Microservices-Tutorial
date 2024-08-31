package be.jimsa.iotdevice.ws.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.web.JsonPath;

@Data
@Builder
public class DeviceDto {

    @JsonProperty("public_id")
    private String publicId;

    private String name;

    @JsonProperty("port_number")
    private int portNumber;
}
