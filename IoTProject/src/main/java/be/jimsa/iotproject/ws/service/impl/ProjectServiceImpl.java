package be.jimsa.iotproject.ws.service.impl;

import be.jimsa.iotproject.config.exception.InternalServiceException;
import be.jimsa.iotproject.config.exception.NotFoundResourceException;
import be.jimsa.iotproject.config.exception.ResourceAlreadyExistException;
import be.jimsa.iotproject.config.log.EvaluateExecuteTimeout;
import be.jimsa.iotproject.utility.id.PublicIdGenerator;
import be.jimsa.iotproject.utility.mapper.ProjectMapper;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import be.jimsa.iotproject.ws.repository.ProjectRepository;
import be.jimsa.iotproject.ws.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static be.jimsa.iotproject.utility.constant.ProjectConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final PublicIdGenerator publicIdGenerator;


    @EvaluateExecuteTimeout
    @Override
    public ProjectDto createNewProject(ProjectDto projectDto) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findByNameAndType(projectDto.getName(), projectDto.getType());
        if (projectEntityOptional.isPresent()) {
            throw new ResourceAlreadyExistException(EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE);
        }
        projectDto.setPublicId(publicIdGenerator.generatePublicId(PUBLIC_ID_DEFAULT_LENGTH));
        ProjectEntity projectEntity = projectMapper.mapToEntity(projectDto);
        projectEntity = projectRepository.save(projectEntity);
        return projectMapper.mapToDto(projectEntity);
    }

    @EvaluateExecuteTimeout
    @Override
    public List<ProjectDto> findAllProjects(int page, int size, String sortField, String sortDirection) {
        Sort sort;
        if (sortField.equalsIgnoreCase("name")) {
            if (sortDirection.equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Direction.ASC, "name");
            } else {
                sort = Sort.by(Sort.Direction.DESC, "name");
            }
        } else {
            if (sortDirection.equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Direction.ASC, "type");
            } else {
                sort = Sort.by(Sort.Direction.DESC, "type");
            }
        }
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                sort
        );
        Page<ProjectEntity> projectEntityPage = projectRepository.findAll(pageable);

        log.info("Total Elements: {}", projectEntityPage.getTotalElements());
        log.info("Total Pages: {}", projectEntityPage.getTotalPages());
        log.info("Number of Elements: {}", projectEntityPage.getNumberOfElements());
        log.info("Size: {}", projectEntityPage.getSize());

        return projectEntityPage
                .get()
                .map(projectMapper::mapToDto)
                .toList();
    }

    @EvaluateExecuteTimeout
    @Override
    public ProjectDto findAProject(String publicId) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findByPublicId(publicId);
        if (projectEntityOptional.isPresent()) {
            return projectMapper.mapToDto(projectEntityOptional.get());
        } else {
            throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }
    }

    @EvaluateExecuteTimeout
    @Override
    public ProjectDto updateAProject(String publicId, ProjectDto newProjectDto) {
        int countOfRowEffected = projectRepository.updateProjectEntity(
                newProjectDto.getName(),
                newProjectDto.getType(),
                publicId
        );
        if (countOfRowEffected == 1) {
            newProjectDto.setPublicId(publicId);
            return newProjectDto;
        } else if (countOfRowEffected == 0) {
            throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        } else {
            throw new InternalServiceException(EXCEPTION_INTERNAL_SERVICE_MESSAGE);
        }
    }

    @EvaluateExecuteTimeout
    @Override
    public ProjectDto patchAProject(String publicId, ProjectDto newProjectDto) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findByPublicId(publicId);
        if (projectEntityOptional.isPresent()) {
            ProjectEntity projectEntity = projectEntityOptional.get();
            if (newProjectDto.getName() != null) {
                projectEntity.setName(newProjectDto.getName());
            }
            if (newProjectDto.getType() != null) {
                projectEntity.setType(newProjectDto.getType());
            }
            projectEntity = projectRepository.save(projectEntity);
            return projectMapper.mapToDto(projectEntity);
        } else {
            throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }
    }

    @EvaluateExecuteTimeout
    @Override
    public boolean removeAProject(String publicId) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findByPublicId(publicId);
        if (projectEntityOptional.isPresent()) {
            projectRepository.delete(projectEntityOptional.get());
            return true;
        } else {
            throw new NotFoundResourceException(EXCEPTION_NOT_FOUND_RESOURCE_MESSAGE);
        }
    }

    @EvaluateExecuteTimeout
    @Override
    public boolean removeAllProjects() {
        projectRepository.deleteAll();
        return true;
    }
}
