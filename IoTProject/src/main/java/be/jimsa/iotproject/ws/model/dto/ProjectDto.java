package be.jimsa.iotproject.ws.model.dto;

import be.jimsa.iotproject.config.validation.annotation.ValidPublicId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import static be.jimsa.iotproject.utility.constant.ProjectConstants.*;

@Data
@Builder
@Schema(
        name = "Project",
        description = "This is 'ProjectDto'"
)
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
    @ValidPublicId(groups = Read.class)
    @Null(message = VALIDATION_PUBLIC_ID_NULL_MESSAGE,
            groups = {Create.class, PutUpdate.class, PatchUpdate.class})
    @Schema(
            description = "read-only field"
    )
    private String publicId;

    @NotEmpty(message = VALIDATION_NAME_NOT_EMPTY_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @NotBlank(message = VALIDATION_NAME_NOT_BLANK_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @Size(min = VALIDATION_NAME_MIN_LENGTH, max = VALIDATION_NAME_MAX_LENGTH, message = VALIDATION_NAME_SIZE_MESSAGE,
            groups = {Read.class, Create.class, PutUpdate.class})
    @Pattern(regexp = VALIDATION_NAME_PATTERN, message = VALIDATION_NAME_SIZE_MESSAGE,
            groups = PatchUpdate.class)
    @Schema(
            description = "name of project", example = "Alpha w1"
    )
    private String name;

    @NotEmpty(message = VALIDATION_TYPE_NOT_EMPTY_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @NotBlank(message = VALIDATION_TYPE_NOT_BLANK_MESSAGE, groups = {Read.class, Create.class, PutUpdate.class})
    @Size(min = VALIDATION_TYPE_MIN_LENGTH, max = VALIDATION_TYPE_MAX_LENGTH, message = VALIDATION_TYPE_SIZE_MESSAGE,
            groups = {Read.class, Create.class, PutUpdate.class})
    @Pattern(regexp = VALIDATION_TYPE_PATTERN, message = VALIDATION_TYPE_SIZE_MESSAGE,
            groups = PatchUpdate.class)
    @Schema(
            description = "type of project", example = "Super lux"
    )
    private String type;
}
