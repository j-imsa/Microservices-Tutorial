package be.jimsa.iotproject.ws.service.impl;

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
        // check
        if (isItValidToCreate(projectDto)) {
            throw new RuntimeException("BAD Request");
        }
        // dto -> publicId
        projectDto.setPublicId(publicIdGenerator.generatePublicId(ProjectConstants.PUBLIC_ID_LENGTH));
        // dto -> entity
        ProjectEntity projectEntity = projectMapper.mapToEntity(projectDto);
        // entity -> save -> entity
        ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);
        // entity -> dto
        ProjectDto returnProjectDto = projectMapper.mapToDto(savedProjectEntity);
        return returnProjectDto;
    }

    public boolean isItValidToCreate(ProjectDto projectDto) {
        return projectDto.getPublicId() != null;
    }
}
