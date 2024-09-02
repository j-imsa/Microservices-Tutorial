package be.jimsa.iotproject.ws.controller;

import be.jimsa.iotproject.config.document.annotation.*;
import be.jimsa.iotproject.config.validation.annotation.ValidPublicId;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import be.jimsa.iotproject.ws.service.ProjectService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/projects", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "Project endpoint",
        description = "CRUD Rest APIs project details"
)
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PostRequestDocument(
            summary = "Create a new project",
            description = "Create a new project using POST method and getting request body"
    )
    public ResponseEntity<ResponseDto> createProject(
            @Validated(ProjectDto.Create.class) @RequestBody ProjectDto projectDto
    ) {
        ProjectDto savedProjectDto = projectService.createNewProject(projectDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(savedProjectDto)
                                .build()
                );
    }

    @GetMapping
    @GetAllRequestDocument(
            summary = "Read all projects",
            description = "Read all projects using GET method"
    )
    public ResponseEntity<ResponseDto> getProjects(
            @Positive(message = ProjectConstants.VALIDATION_PAGE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = ProjectConstants.VALIDATION_PAGE_DEFAULT_VALUE)
            @Parameter(example = "3")
            int page,
            @Positive(message = ProjectConstants.VALIDATION_SIZE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = ProjectConstants.VALIDATION_SIZE_DEFAULT_VALUE)
            @Parameter(example = "25")
            int size,
            @Pattern(regexp = ProjectConstants.VALIDATION_SORT_FIELD_PATTERN,
                    message = ProjectConstants.VALIDATION_SORT_FIELD_PATTERN_MESSAGE)
            @RequestParam(value = ProjectConstants.VALIDATION_SORT_FIELD,
                    defaultValue = ProjectConstants.VALIDATION_SORT_FIELD_DEFAULT_VALUE)
            @Parameter(example = "name", description = "'name' or 'type'")
            String sortField,
            @Pattern(regexp = ProjectConstants.VALIDATION_SORT_DIRECTION_PATTERN,
                    message = ProjectConstants.VALIDATION_SORT_DIRECTION_PATTERN_MESSAGE)
            @RequestParam(value = ProjectConstants.VALIDATION_SORT_DIRECTION,
                    defaultValue = ProjectConstants.VALIDATION_SORT_DIRECTION_DEFAULT_VALUE)
            @Parameter(example = "desc", description = "'asc' or 'desc'")
            String sortDirection
    ) {
        List<ProjectDto> foundedlist = projectService.findAllProjects(page, size, sortField, sortDirection);
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(foundedlist)
                                .build()
                );
    }

    @GetMapping("/{public_id}")
    @GetAnObjectRequestDocument(
            summary = "Read a project",
            description = "Read a project using GET method and getting a valid public_id as a parameter"
    )
    public ResponseEntity<ResponseDto> getAProject(
            @PublicIdDocument @ValidPublicId @PathVariable("public_id") String publicId
    ) {
        ProjectDto foundedProjectDto = projectService.findAProject(publicId);
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(foundedProjectDto)
                                .build()
                );
    }

    @PutMapping("/{public_id}")
    @PutRequestDocument(
            summary = "Update a whole project",
            description = "Update a project using PUT method and getting a valid public_id as a parameter and a new project as a request body"
    )
    public ResponseEntity<ResponseDto> updateAProject(
            @PublicIdDocument @ValidPublicId @PathVariable("public_id") String publicId,
            @Validated(ProjectDto.PutUpdate.class) @RequestBody ProjectDto newProjectDto
    ) {
        ProjectDto updatedProjectDto = projectService.updateAProject(publicId, newProjectDto);
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(updatedProjectDto)
                                .build()
                );
    }

    @PatchMapping("/{public_id}")
    @PatchRequestDocument(
            summary = "Update a piece of a project",
            description = "Update a piece of a project using PATCH method and getting a valid public_id as a parameter and a new values as request bodies"
    )
    public ResponseEntity<ResponseDto> patchAProject(
            @PublicIdDocument @ValidPublicId @PathVariable("public_id") String publicId,
            @Validated(ProjectDto.PatchUpdate.class) @RequestBody ProjectDto newProjectDto
    ) {
        ProjectDto updatedProjectDto = projectService.patchAProject(publicId, newProjectDto);
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(updatedProjectDto)
                                .build()
                );
    }

    @DeleteMapping("/{public_id}")
    @DeleteAnObjectRequestDocument(
            summary = "Delete a project",
            description = "Delete a project using DELETE method and getting a valid public_id as a parameter"
    )
    public ResponseEntity<ResponseDto> removeAProject(
            @PublicIdDocument @ValidPublicId @PathVariable("public_id") String publicId
    ) {
        boolean result = projectService.removeAProject(publicId);
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(result)
                                .build()
                );
    }

    @DeleteMapping
    @DeleteAllRequestDocument(
            summary = "Delete all projects",
            description = "Delete all projects using DELETE method"
    )
    public ResponseEntity<ResponseDto> removeProjects() {
        boolean result = projectService.removeAllProjects();
        return ResponseEntity
                .ok(
                        ResponseDto.builder()
                                .action(true)
                                .timestamp(LocalDateTime.now())
                                .result(result)
                                .build()
                );
    }


}
