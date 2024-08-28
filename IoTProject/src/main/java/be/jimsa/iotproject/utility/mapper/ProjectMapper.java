package be.jimsa.iotproject.utility.mapper;

import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectMapper {

    public Optional<ProjectEntity> mapToEntity(ProjectDto projectDto) {
        if (projectDto == null || projectDto.getPublicId() == null || projectDto.getName() == null || projectDto.getType() == null) {
            return Optional.empty();
        }
        return Optional.of(ProjectEntity.builder().publicId(projectDto.getPublicId()).name(projectDto.getName()).type(projectDto.getType()).build());
    }

    public Optional<ProjectDto> mapToDto(ProjectEntity projectEntity) {
        if (projectEntity == null || projectEntity.getPublicId() == null || projectEntity.getName() == null || projectEntity.getType() == null) {
            return Optional.empty();
        }
        return Optional.of(ProjectDto.builder().publicId(projectEntity.getPublicId()).name(projectEntity.getName()).type(projectEntity.getType()).build());
    }
}
