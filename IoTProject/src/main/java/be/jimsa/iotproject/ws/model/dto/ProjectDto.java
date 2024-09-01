package be.jimsa.iotproject.ws.model.dto;

import be.jimsa.iotproject.utility.constant.ProjectConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.sql.Update;

import static be.jimsa.iotproject.utility.constant.ProjectConstants.*;

@Data
@Builder
public class ProjectDto {

    public interface Create {
    }

    public interface Read {
    }

    public interface PutUpdate {
    }
    public interface PatchUpdate {
    }

    @JsonProperty(VALIDATION_PUBLIC_ID)
    @NotEmpty(message = VALIDATION_PUBLIC_ID_NOT_EMPTY_MESSAGE, groups = Read.class)
    @NotBlank(message = VALIDATION_PUBLIC_ID_NOT_BLANK_MESSAGE, groups = Read.class)
    @Size(min = VALIDATION_PUBLIC_ID_MIN,
            max = VALIDATION_PUBLIC_ID_MAX,
            message = VALIDATION_PUBLIC_ID_SIZE_MESSAGE,
            groups = Read.class)
    @Pattern(regexp = VALIDATION_PUBLIC_ID_PATTERN,
            message = VALIDATION_PUBLIC_ID_PATTERN_MESSAGE,
            groups = Read.class)
    @Null(message = VALIDATION_PUBLIC_ID_NULL_MESSAGE,
            groups = {Create.class, PutUpdate.class, PatchUpdate.class})
    private String publicId;

    @NotEmpty(message = VALIDATION_NAME_NOT_EMPTY_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @NotBlank(message = VALIDATION_NAME_NOT_BLANK_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @Size(min = VALIDATION_NAME_MIN, max = VALIDATION_NAME_MAX, message = VALIDATION_NAME_SIZE_MESSAGE,
            groups = {Read.class, Create.class, PutUpdate.class})
    @Pattern(regexp = VALIDATION_NAME_PATTERN, message = VALIDATION_NAME_SIZE_MESSAGE,
            groups = PatchUpdate.class)
    private String name;

    @NotEmpty(message = VALIDATION_TYPE_NOT_EMPTY_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @NotBlank(message = VALIDATION_TYPE_NOT_BLANK_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @Size(min = VALIDATION_TYPE_MIN, max = VALIDATION_TYPE_MAX, message = VALIDATION_TYPE_SIZE_MESSAGE,
            groups = {Read.class, Create.class, PutUpdate.class})
    @Pattern(regexp = VALIDATION_TYPE_PATTERN, message = VALIDATION_TYPE_SIZE_MESSAGE,
            groups = PatchUpdate.class)
    private String type;
}
