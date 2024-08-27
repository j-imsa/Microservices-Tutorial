package be.jimsa.iotproject.ws.controller;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import be.jimsa.iotproject.ws.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/v1/projects", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ResponseDto> createProject(
            @RequestBody ProjectDto projectDto
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
}
