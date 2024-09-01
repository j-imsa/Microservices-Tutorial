package be.jimsa.iotproject.ws.controller;

import be.jimsa.iotproject.config.validation.annotation.ValidPublicId;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import be.jimsa.iotproject.ws.service.ProjectService;
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
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
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
    public ResponseEntity<ResponseDto> getProjects(
            @Positive(message = ProjectConstants.VALIDATION_PAGE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = ProjectConstants.VALIDATION_PAGE_DEFAULT_VALUE) int page,
            @Positive(message = ProjectConstants.VALIDATION_SIZE_POSITIVE_INTEGER)
            @RequestParam(defaultValue = ProjectConstants.VALIDATION_SIZE_DEFAULT_VALUE) int size,
            @Pattern(regexp = ProjectConstants.VALIDATION_SORT_FIELD_PATTERN,
                    message = ProjectConstants.VALIDATION_SORT_FIELD_PATTERN_MESSAGE)
            @RequestParam(value = ProjectConstants.VALIDATION_SORT_FIELD,
                    defaultValue = ProjectConstants.VALIDATION_SORT_FIELD_DEFAULT_VALUE) String sortField,
            @Pattern(regexp = ProjectConstants.VALIDATION_SORT_DIRECTION_PATTERN,
                    message = ProjectConstants.VALIDATION_SORT_DIRECTION_PATTERN_MESSAGE)
            @RequestParam(value = ProjectConstants.VALIDATION_SORT_DIRECTION,
                    defaultValue = ProjectConstants.VALIDATION_SORT_DIRECTION_DEFAULT_VALUE) String sortDirection
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
    public ResponseEntity<ResponseDto> getAProject(
            @ValidPublicId @PathVariable("public_id") String publicId
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
    public ResponseEntity<ResponseDto> updateAProject(
            @ValidPublicId @PathVariable("public_id") String publicId,
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
    public ResponseEntity<ResponseDto> patchAProject(
            @ValidPublicId @PathVariable("public_id") String publicId,
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
    public ResponseEntity<ResponseDto> removeAProject(
            @ValidPublicId @PathVariable("public_id") String publicId
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
