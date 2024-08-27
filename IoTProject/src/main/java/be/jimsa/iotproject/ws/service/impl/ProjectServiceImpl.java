package be.jimsa.iotproject.ws.service.impl;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.repository.ProjectRepository;
import be.jimsa.iotproject.ws.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectDto createNewProject(ProjectDto projectDto) {
        if (projectDto.getPublicId() != null) {
            throw new RuntimeException("BAD Request");
        }
        // dto -> entity
        // entity -> save -> entity
        // entity -> dto
        return null;
    }
}
