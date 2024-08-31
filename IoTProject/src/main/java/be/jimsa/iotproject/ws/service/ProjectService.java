package be.jimsa.iotproject.ws.service;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto createNewProject(ProjectDto projectDto);

    List<ProjectDto> findAllProjects();

    ProjectDto findAProject(String publicId);

    ProjectDto updateAProject(String publicId, ProjectDto newProjectDto);

    ProjectDto patchAProject(String publicId, ProjectDto newProjectDto);

    boolean removeAProject(String publicId);

    boolean removeAllProjects();

}
