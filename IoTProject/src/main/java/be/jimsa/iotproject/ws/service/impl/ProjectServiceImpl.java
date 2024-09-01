package be.jimsa.iotproject.ws.service.impl;

import be.jimsa.iotproject.config.exception.BadFormatRequestException;
import be.jimsa.iotproject.config.exception.InternalServiceException;
import be.jimsa.iotproject.config.exception.NullObjectException;
import be.jimsa.iotproject.config.exception.ResourceAlreadyExistException;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import be.jimsa.iotproject.utility.id.PublicIdGenerator;
import be.jimsa.iotproject.utility.mapper.ProjectMapper;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import be.jimsa.iotproject.ws.repository.ProjectRepository;
import be.jimsa.iotproject.ws.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final PublicIdGenerator publicIdGenerator;


    @Override
    public ProjectDto createNewProject(ProjectDto projectDto) {
        return null;
    }

    @Override
    public List<ProjectDto> findAllProjects(int page, int size, String sortField, String sortDirection) {
        return List.of();
    }

    @Override
    public ProjectDto findAProject(String publicId) {
        return null;
    }

    @Override
    public ProjectDto updateAProject(String publicId, ProjectDto newProjectDto) {
        return null;
    }

    @Override
    public ProjectDto patchAProject(String publicId, ProjectDto newProjectDto) {
        return null;
    }

    @Override
    public boolean removeAProject(String publicId) {
        return false;
    }

    @Override
    public boolean removeAllProjects() {
        return false;
    }
}
