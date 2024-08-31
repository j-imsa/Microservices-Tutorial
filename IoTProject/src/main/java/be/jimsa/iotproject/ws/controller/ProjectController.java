package be.jimsa.iotproject.ws.controller;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.dto.ResponseDto;
import be.jimsa.iotproject.ws.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<ResponseDto> getProjects() {
        List<ProjectDto> foundedlist = projectService.findAllProjects();
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
            @PathVariable("public_id") String publicId
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
            @PathVariable("public_id") String publicId,
            @RequestBody ProjectDto newProjectDto
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
            @PathVariable("public_id") String publicId,
            @RequestBody ProjectDto newProjectDto
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
            @PathVariable("public_id") String publicId
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
