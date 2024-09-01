package be.jimsa.iotproject.ws.model.entity;

import be.jimsa.iotproject.config.validation.annotation.ValidPublicId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import static be.jimsa.iotproject.utility.constant.ProjectConstants.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "projects")
public class ProjectEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true)
    @ValidPublicId
    private String publicId;

    @Column(nullable = false)
    @NotBlank(message = VALIDATION_NAME_NOT_BLANK_MESSAGE)
    @NotEmpty(message = VALIDATION_NAME_NOT_EMPTY_MESSAGE)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = VALIDATION_TYPE_NOT_BLANK_MESSAGE)
    @NotEmpty(message = VALIDATION_TYPE_NOT_EMPTY_MESSAGE)
    private String type;
}
