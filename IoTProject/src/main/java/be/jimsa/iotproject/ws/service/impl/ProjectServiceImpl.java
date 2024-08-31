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

        if (projectDto == null) {
            throw new NullObjectException(ProjectConstants.EXCEPTION_NULL_MESSAGE + ProjectConstants.PROJECT_NAME);
        }
        if (!isItValidToCreate(projectDto)) {
            throw new BadFormatRequestException(ProjectConstants.EXCEPTION_BAD_FORMAT_MESSAGE + ProjectConstants.PROJECT_ITEM_PUBLIC_ID);
        }
        if (isItExistIntoDatabase(projectDto)) {
            throw new ResourceAlreadyExistException(ProjectConstants.EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE + String.format("{%s:'%s', %s:'%s'}", ProjectConstants.PROJECT_ITEM_NAME, projectDto.getName(), ProjectConstants.PROJECT_ITEM_TYPE, projectDto.getType()));
        }

        projectDto.setPublicId(publicIdGenerator.generatePublicId(ProjectConstants.PUBLIC_ID_LENGTH));
        Optional<ProjectEntity> projectEntityOptional = projectMapper.mapToEntity(projectDto);
        return projectEntityOptional
                .map(projectEntity -> {
                    ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);
                    Optional<ProjectDto> projectDtoOptional = projectMapper.mapToDto(savedProjectEntity);
                    return projectDtoOptional
                            .orElseThrow(() -> {
                                projectRepository.delete(savedProjectEntity);
                                return new InternalServiceException(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE + ProjectConstants.PROJECT_CAST);
                            });
                })
                .orElseThrow(() -> new InternalServiceException(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE + ProjectConstants.PROJECT_CAST));
    }

    @Override
    public List<ProjectDto> findAllProjects() {
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

    public boolean isItExistIntoDatabase(ProjectDto projectDto) {
        if (projectDto != null) {
            return projectRepository.findByNameAndType(projectDto.getName(), projectDto.getType()).isPresent();
        } else {
            return false;
        }
    }

    public boolean isItValidToCreate(ProjectDto projectDto) {
        if (projectDto != null) {
            return projectDto.getPublicId() == null;
        } else {
            return false;
        }
    }
}
