package be.jimsa.iotproject.utility;

import be.jimsa.iotproject.utility.mapper.ProjectMapper;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProjectMapperTests {

    private ProjectMapper projectMapper;

    private ProjectDto projectDto;
    private ProjectEntity projectEntity;
    private static final String PUBLIC_ID = "Z5ugVmutiGhLIlFHmPE";
    private static final String NAME = "Alpha w1";
    private static final String TYPE = "Super Lux";

    @BeforeEach
    public void setUp() {
        projectMapper = new ProjectMapper();
    }

    @Nested
    @DisplayName("tests for MapToEntity")
    class MapToEntityTests {
        @Test
        @DisplayName("testMapToEntityProjectMapper_withAValidDto_shouldReturnAValidOptionalEntity")
        void givenAValidDto_whenMapToEntity_thenShouldReturnAValidEntity() {
            projectDto = ProjectDto.builder().publicId(PUBLIC_ID).name(NAME).type(TYPE).build();

            Optional<ProjectEntity> result = projectMapper.mapToEntity(projectDto);

            assertThat(result).isNotNull().isInstanceOf(Optional.class).isPresent().hasValueSatisfying(entity -> assertThat(entity).isInstanceOf(ProjectEntity.class)).hasValueSatisfying(entity -> {
                assertThat(entity.getPublicId()).isEqualTo(PUBLIC_ID);
                assertThat(entity.getName()).isEqualTo(NAME);
                assertThat(entity.getType()).isEqualTo(TYPE);
                assertThat(entity.getId()).isNull();
            });
        }

        @Test
        @DisplayName("testMapToEntityProjectMapper_withANullDto_shouldReturnAnEmptyOptional")
        void givenANullDto_whenMapToEntity_thenShouldReturnAnEmptyOptional() {
            projectDto = null;
            Optional<ProjectEntity> result = projectMapper.mapToEntity(projectDto);
            assertThat(result).isNotNull().isInstanceOf(Optional.class).isEmpty();
        }

        @Test
        @DisplayName("testMapToEntityProjectMapper_withANullPublicId_shouldReturnAnEmptyOptional")
        void givenANullPublicIdDto_whenMapToEntity_thenShouldReturnAnEmptyOptional() {
            projectDto = ProjectDto.builder().name(NAME).type(TYPE).build();

            Optional<ProjectEntity> result = projectMapper.mapToEntity(projectDto);
            assertThat(result).isNotNull().isInstanceOf(Optional.class).isEmpty();
        }

        @Test
        @DisplayName("testMapToEntityProjectMapper_withANullName_shouldReturnAnEmptyOptional")
        void givenANullNameDto_whenMapToEntity_thenShouldReturnAnEmptyOptional() {
            projectDto = ProjectDto.builder().publicId(PUBLIC_ID).type(TYPE).build();

            Optional<ProjectEntity> result = projectMapper.mapToEntity(projectDto);
            assertThat(result).isNotNull().isInstanceOf(Optional.class).isEmpty();
        }

        @Test
        @DisplayName("testMapToEntityProjectMapper_withANullType_shouldReturnAnEmptyOptional")
        void givenANullTypeDto_whenMapToEntity_thenShouldReturnAnEmptyOptional() {
            projectDto = ProjectDto.builder().publicId(PUBLIC_ID).name(NAME).build();

            Optional<ProjectEntity> result = projectMapper.mapToEntity(projectDto);
            assertThat(result).isNotNull().isInstanceOf(Optional.class).isEmpty();
        }

    }

    @Nested
    @DisplayName("tests for MapToDto")
    class MapToDtoTests {
        @Test
        @DisplayName("testMapToDtoProjectMapper_withAValidEntity_shouldReturnAValidOptionalDto")
        void givenAValidEntity_whenMapToDto_thenShouldReturnAValidOptionalDto() {
            projectEntity = ProjectEntity.builder().id(100L).publicId(PUBLIC_ID).name(NAME).type(TYPE).build();

            Optional<ProjectDto> result = projectMapper.mapToDto(projectEntity);

            assertThat(result).isNotNull().isInstanceOf(Optional.class).isPresent().hasValueSatisfying(entity -> assertThat(entity).isInstanceOf(ProjectDto.class)).hasValueSatisfying(entity -> {
                assertThat(entity.getPublicId()).isEqualTo(PUBLIC_ID);
                assertThat(entity.getName()).isEqualTo(NAME);
                assertThat(entity.getType()).isEqualTo(TYPE);
            });

        }

        @Test
        @DisplayName("testMapToDtoProjectMapper_withANullEntity_shouldReturnAnEmptyOptionalDto")
        void givenANullEntity_whenMapToDto_thenShouldReturnAnEmptyOptionalDto() {
            projectEntity = null;
            Optional<ProjectDto> result = projectMapper.mapToDto(projectEntity);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("testMapToDtoProjectMapper_withANullPublicIdEntity_shouldReturnAnEmptyOptionalDto")
        void givenANullPublicIdEntity_whenMapToDto_thenShouldReturnAnEmptyOptionalDto() {
            projectEntity = ProjectEntity.builder().id(100L).name(NAME).type(TYPE).build();
            Optional<ProjectDto> result = projectMapper.mapToDto(projectEntity);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("testMapToDtoProjectMapper_withANullNameEntity_shouldReturnAnEmptyOptionalDto")
        void givenANullNameEntity_whenMapToDto_thenShouldReturnAnEmptyOptionalDto() {
            projectEntity = ProjectEntity.builder().id(100L).publicId(PUBLIC_ID).type(TYPE).build();
            Optional<ProjectDto> result = projectMapper.mapToDto(projectEntity);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("testMapToDtoProjectMapper_withANullTypeEntity_shouldReturnAnEmptyOptionalDto")
        void givenANullTypeEntity_whenMapToDto_thenShouldReturnAnEmptyOptionalDto() {
            projectEntity = ProjectEntity.builder().id(100L).publicId(PUBLIC_ID).name(NAME).build();
            Optional<ProjectDto> result = projectMapper.mapToDto(projectEntity);
            assertThat(result).isEmpty();
        }

    }

}
