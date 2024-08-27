package be.jimsa.iotproject.ws.service;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;

public interface ProjectService {
    ProjectDto createNewProject(ProjectDto projectDto);
}
