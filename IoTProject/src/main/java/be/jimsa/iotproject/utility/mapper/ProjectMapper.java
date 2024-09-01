package be.jimsa.iotproject.utility.mapper;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectEntity mapToEntity(ProjectDto projectDto) {
        return ProjectEntity.builder()
                .publicId(projectDto.getPublicId())
                .name(projectDto.getName())
                .type(projectDto.getType())
                .build();
    }

    public ProjectDto mapToDto(ProjectEntity projectEntity) {
        return ProjectDto.builder()
                .publicId(projectEntity.getPublicId())
                .name(projectEntity.getName())
                .type(projectEntity.getType())
                .build();
    }
}
