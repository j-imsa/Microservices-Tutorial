package be.jimsa.iotproject.ws.service.impl;

import be.jimsa.iotproject.config.exception.BadFormatRequestException;
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
            throw new ResourceAlreadyExistException(ProjectConstants.EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE +
                    String.format("{%s:'%s', %s:'%s'}", ProjectConstants.PROJECT_ITEM_NAME, projectDto.getName(),
                            ProjectConstants.PROJECT_ITEM_TYPE, projectDto.getType())
            );
        }

        projectDto.setPublicId(
                publicIdGenerator.generatePublicId(ProjectConstants.PUBLIC_ID_LENGTH)
        );

        ProjectEntity projectEntity = projectMapper.mapToEntity(projectDto);

        ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);

        return projectMapper.mapToDto(savedProjectEntity);
    }

    private boolean isItExistIntoDatabase(ProjectDto projectDto) {
        if (projectDto != null) {
            return projectRepository
                    .findByNameAndType(projectDto.getName(), projectDto.getType())
                    .isPresent();
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
