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
    @DisplayName("IsItValidToCreateProject")
    class IsItValidToCreateProjectTests {
        @Test
        @DisplayName("by a valid dto, without public_id, should return true")
        @Order(1)
        void givenAValidDto_whenIsItValidToCreate_thenReturnTrue() {
            ProjectDto projectDto = ProjectDto.builder().name(projectName).type(projectType).build();

            boolean result = projectServiceImpl.isItValidToCreate(projectDto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("by an invalid dto, with public_id, should return false")
        @Order(2)
        void givenAnInvalidDtoWithPublicId_whenIsItValidToCreate_thenReturnFalse() {
            ProjectDto projectDto = ProjectDto.builder().publicId(projectPublicId).name(projectName).type(projectType).build();

            boolean result = projectServiceImpl.isItValidToCreate(projectDto);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("by null object, should return false")
        @Order(3)
        void givenANullObject_whenIsItValidToCreate_thenReturnFalse() {
            ProjectDto projectDto = null;

            boolean result = projectServiceImpl.isItValidToCreate(projectDto);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("IsItExistIntoDatabase")
    class IsItExistIntoDatabaseTests {

        @Test
        @DisplayName("by a valid dto, not exist in db, should return true")
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
        @DisplayName("by a null object, should return false")
        void givenANullObject_whenIsItExistIntoDatabase_thenReturnFalse() {
            ProjectDto projectDto = null;
            boolean result = projectServiceImpl.isItExistIntoDatabase(projectDto);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("by a valid dto, with does not exist name and type, should return false")
        void givenAValidDtoAndDoesNotExistNameType_whenIsItExistIntoDatabase_thenReturnFalse() {
            ProjectDto projectDto = ProjectDto.builder().publicId(projectPublicId).name(projectName).type(projectType).build();
            given(projectRepository.findByNameAndType(anyString(), anyString())).willReturn(Optional.empty());

            // when - action or the behaviour that we are going test:
            boolean result = projectServiceImpl.isItExistIntoDatabase(projectDto);

            // then - verify the output:
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("by a valid dto, with exist name and type, should return true")
        void givenAValidDtoAndExistNameType_whenIsItExistIntoDatabase_thenReturnTrue() {
            ProjectDto projectDto = ProjectDto.builder().publicId(projectPublicId).name(projectName).type(projectType).build();
            given(projectRepository.findByNameAndType(anyString(), anyString()))
                    .willReturn(Optional.of(ProjectEntity.builder().build()));

            // when - action or the behaviour that we are going test:
            boolean result = projectServiceImpl.isItExistIntoDatabase(projectDto);

            // then - verify the output:
            assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("CreateNewProject")
    class CreateNewProjectTests {

        @Test
        @DisplayName("by a valid project-dto, should return saved project-dto")
        void givenAValidProjectDto_whenCreateNewProject_thenReturnSavedProjectDto() {
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
        @DisplayName("by a null object, should throw NullObjectException")
        void givenANullObject_whenCreateNewProject_thenThrowNullObjectException() {
            ProjectDto inputProjectDto = null;
            assertThatThrownBy(() -> projectServiceImpl.createNewProject(inputProjectDto))
                    .isInstanceOf(NullObjectException.class)
                    .hasMessageContaining(ProjectConstants.EXCEPTION_NULL_MESSAGE);
        }

        @Test
        @DisplayName("by a project-dto which has public_id, should throw BadFormatRequestException")
        void givenAnInvalidProjectDtoWithPublicId_whenCreateNewProject_thenThrowBadFormatRequestException() {
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
        @DisplayName("by an exist entity, should throw ResourceAlreadyExistException")
        void givenAnExistProjectEntity_whenCreateNewProject_thenThrowResourceAlreadyExistException() {
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
        @DisplayName("can not cast dto-to-entity, should throw InternalServiceException")
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
        @DisplayName("can not cast entity-to-dto, should throw InternalServiceException")
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

    @Nested
    @DisplayName("FindAllProjects")
    class FindAllProjectsTests {
        @Test
        @DisplayName("")
        void given_when_then() {
            // given - precondition or setup:

            // when - action or the behaviour that we are going test:

            // then - verify the output:

        }
    }

}
