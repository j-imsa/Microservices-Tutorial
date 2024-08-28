package be.jimsa.iotproject.unit.service;

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
import be.jimsa.iotproject.ws.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTests {

    @InjectMocks
    private ProjectServiceImpl projectServiceImpl;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Spy
    private PublicIdGenerator publicIdGenerator;

    private String projectName;
    private String projectType;
    private String projectPublicId;
    private long projectId;

    @BeforeEach
    void setUp() {
        projectName = "Alpha w1";
        projectType = "Luxury";
        projectPublicId = publicIdGenerator.generatePublicId(64);
        projectId = 100L;
    }

    @Nested
    @DisplayName("test for IsItValidToCreateProject")
    class IsItValidToCreateProjectTests {
        @Test
        @DisplayName("testIsItValidToCreateProject_withValidObject_shouldReturnTrue")
        @Order(1)
        void givenAValidObject_whenIsItValidToCreate_thenReturnTrue() {
            ProjectDto projectDto = ProjectDto.builder().name(projectName).type(projectType).build();

            boolean result = projectServiceImpl.isItValidToCreate(projectDto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("testIsItValidToCreateProject_withoutValidObject_shouldReturnFalse")
        @Order(2)
        void givenAWrongObject_whenIsItValidToCreate_thenReturnFalse() {
            ProjectDto projectDto = ProjectDto.builder().publicId(projectPublicId).name(projectName).type(projectType).build();

            boolean result = projectServiceImpl.isItValidToCreate(projectDto);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("testIsItValidToCreateProject_withNullObject_shouldReturnFalse")
        @Order(3)
        void givenANullObject_whenIsItValidToCreate_thenReturnFalse() {
            ProjectDto projectDto = null;

            boolean result = projectServiceImpl.isItValidToCreate(projectDto);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("test for IsItExistIntoDatabase")
    class IsItExistIntoDatabaseTests {

        @Test
        @DisplayName("testIsItExistIntoDatabaseProjectServiceImpl_withAValidDto_shouldReturnTrue")
        void givenAValidDto_whenIsItExistIntoDatabase_thenReturnTrue() {
            // given - precondition or setup:
            ProjectDto projectDto = ProjectDto.builder().publicId(projectPublicId).name(projectName).type(projectType).build();
            ProjectEntity projectEntity = ProjectEntity.builder().id(projectId).publicId(projectPublicId).name(projectName).type(projectType).build();
            given(projectRepository.findByNameAndType(anyString(), anyString())).willReturn(Optional.of(projectEntity));

            // when - action or the behaviour that we are going test:
            boolean result = projectServiceImpl.isItExistIntoDatabase(projectDto);

            // then - verify the output:
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("testIsItExistIntoDatabaseProjectServiceImpl_withANullDto_shouldReturnFalse")
        void givenANullDto_whenIsItExistIntoDatabase_thenReturnFalse() {
            ProjectDto projectDto = null;
            boolean result = projectServiceImpl.isItExistIntoDatabase(projectDto);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("testIsItExistIntoDatabaseProjectServiceImpl_withAValidDtoAndEmptyResult_shouldReturnFalse")
        void givenAValidDtoAndEmptyResult_whenIsItExistIntoDatabase_thenReturnFalse() {
            ProjectDto projectDto = ProjectDto.builder().publicId(projectPublicId).name(projectName).type(projectType).build();
            given(projectRepository.findByNameAndType(anyString(), anyString())).willReturn(Optional.empty());

            // when - action or the behaviour that we are going test:
            boolean result = projectServiceImpl.isItExistIntoDatabase(projectDto);

            // then - verify the output:
            assertThat(result).isFalse();
        }

    }

    @Nested
    @DisplayName("test for CreateNewProject")
    class CreateNewProjectTests {

        @Test
        @DisplayName("testCreateNewProjectProject_withAValidObject_shouldReturnProjectDto")
        void givenAValidObject_whenCreateNewProject_thenReturnSavedProjectDto() {
            ProjectDto inputProjectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            ProjectEntity savedProjectEntity = ProjectEntity.builder()
                    .id(projectId)
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            ProjectDto result = ProjectDto.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectMapper.mapToEntity(any(ProjectDto.class)))
                    .willReturn(Optional.of(projectEntity));
            given(projectRepository.save(projectEntity))
                    .willReturn(savedProjectEntity);
            given(projectMapper.mapToDto(savedProjectEntity))
                    .willReturn(Optional.of(result));

            ProjectDto actualProjectDto = projectServiceImpl.createNewProject(inputProjectDto);

            assertThat(actualProjectDto)
                    .isNotNull()
                    .isInstanceOf(ProjectDto.class);
            assertThat(actualProjectDto.getName()).isEqualTo(projectName);
            assertThat(actualProjectDto.getType()).isEqualTo(projectType);
            assertThat(actualProjectDto.getPublicId()).isEqualTo(projectPublicId);
            verify(projectRepository, times(1)).save(any(ProjectEntity.class));
            verify(projectRepository, never()).delete(any(ProjectEntity.class));

            assertTimeout(Duration.ofMillis(10), () -> projectServiceImpl.createNewProject(
                    ProjectDto.builder()
                            .name("New Name")
                            .type("New Type")
                            .build()
            ));
        }

        @Test
        @DisplayName("testCreateNewProjectProject_withANull_shouldThrowNullObjectException")
        void givenANull_whenCreateNewProject_thenThrowNullObjectException() {
            ProjectDto inputProjectDto = null;
            assertThatThrownBy(() -> projectServiceImpl.createNewProject(inputProjectDto))
                    .isInstanceOf(NullObjectException.class)
                    .hasMessageContaining(ProjectConstants.EXCEPTION_NULL_MESSAGE);
        }

        @Test
        @DisplayName("testCreateNewProjectProject_withAPublicId_shouldThrowBadFormatRequestException")
        void givenAPublicId_whenCreateNewProject_thenThrowBadFormatRequestException() {
            ProjectDto projectDto = ProjectDto.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            assertThatThrownBy(() -> projectServiceImpl.createNewProject(projectDto))
                    .isInstanceOf(BadFormatRequestException.class)
                    .hasMessageContaining(ProjectConstants.EXCEPTION_BAD_FORMAT_MESSAGE);
        }

        @Test
        @DisplayName("testCreateNewProjectProject_withAnExistObject_shouldThrowResourceAlreadyExistException")
        void givenAnExistObject_whenCreateNewProject_thenThrowResourceAlreadyExistException() {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            ProjectEntity existedProjectEntity = ProjectEntity.builder()
                    .id(projectId)
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectRepository.findByNameAndType(anyString(), anyString())).willReturn(Optional.of(existedProjectEntity));

            assertThatThrownBy(() -> projectServiceImpl.createNewProject(projectDto))
                    .isInstanceOf(ResourceAlreadyExistException.class)
                    .hasMessageContaining(ProjectConstants.EXCEPTION_RESOURCE_ALREADY_EXIST_MESSAGE);
        }

        @Test
        @DisplayName("testCreateNewProjectProject_withMapToEntityReturnsEmpty_shouldThrowInternalServiceException")
        void givenMapToEntityReturnsEmpty_whenCreateNewProject_thenThrowInternalServiceException() {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectRepository.findByNameAndType(anyString(), anyString())).willReturn(Optional.empty());
            given(projectMapper.mapToEntity(any(ProjectDto.class))).willReturn(Optional.empty());

            assertThatThrownBy(() -> projectServiceImpl.createNewProject(projectDto))
                    .isInstanceOf(InternalServiceException.class)
                    .hasMessageContaining(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE);
        }

        @Test
        @DisplayName("testCreateNewProjectProject_withMapToDtoReturnsEmpty_shouldThrowInternalServiceException")
        void givenMapToDtoReturnsEmpty_whenCreateNewProject_thenThrowInternalServiceException() {
            ProjectDto projectDto = ProjectDto.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .id(projectId)
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            given(projectRepository.findByNameAndType(anyString(), anyString())).willReturn(Optional.empty());
            given(projectMapper.mapToEntity(any(ProjectDto.class))).willReturn(Optional.of(projectEntity));
            given(projectRepository.save(any(ProjectEntity.class))).willReturn(projectEntity);
            given(projectMapper.mapToDto(any(ProjectEntity.class))).willReturn(Optional.empty());

            assertThatThrownBy(() -> projectServiceImpl.createNewProject(projectDto))
                    .isInstanceOf(InternalServiceException.class)
                    .hasMessageContaining(ProjectConstants.EXCEPTION_INTERNAL_SERVICE_MESSAGE);
            verify(projectRepository, times(1)).save(any(ProjectEntity.class));
            verify(projectRepository, times(1)).delete(any(ProjectEntity.class));
        }

    }

}
