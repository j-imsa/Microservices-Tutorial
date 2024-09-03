package be.jimsa.iotproject.unit.utility;

import be.jimsa.iotproject.utility.mapper.ProjectMapper;
import be.jimsa.iotproject.ws.model.dto.ProjectDto;
import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProjectMapperTests {

    private ProjectMapper projectMapper;

    private String projectId;
    private String projectName;
    private String projectType;

    @BeforeEach
    public void setUp() {
        projectMapper = new ProjectMapper();
        projectId = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e";
        projectName = "Robbert Romeo";
        projectType = "Luxury l2";
    }

    @Nested
    @DisplayName("MapToEntity")
    class MapToEntityTests {

        @Test
        @DisplayName("by a valid dto, should return a valid entity")
        void givenAValidDto_whenMapToEntity_thenReturnAValidEntity() {
            // given(Arrange) - precondition or setup:
            ProjectDto input = ProjectDto.builder()
                    .publicId(projectId)
                    .name(projectName)
                    .type(projectType)
                    .build();

            // when(Act) - action or the behaviour that we are going test:
            ProjectEntity result = projectMapper.mapToEntity(input);

            // then(Assert) - verify the output:
            assertThat(result)
                    .isNotNull()
                    .isInstanceOf(ProjectEntity.class)
                    .hasFieldOrPropertyWithValue("publicId", projectId)
                    .hasFieldOrPropertyWithValue("name", projectName)
                    .hasFieldOrPropertyWithValue("type", projectType)
                    .hasFieldOrPropertyWithValue("id", null);
        }

        @Test
        @DisplayName("by a valid dto with null values, should return a valid entity with null values")
        void givenAValidDtoWithNullValues_whenMapToEntity_thenReturnAValidEntityWithNullValues() {
            // given(Arrange) - precondition or setup:
            ProjectDto input = ProjectDto.builder()
                    .publicId(null)
                    .name(null)
                    .type(null)
                    .build();

            // when(Act) - action or the behaviour that we are going test:
            ProjectEntity result = projectMapper.mapToEntity(input);

            // then(Assert) - verify the output:
            assertThat(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", null)
                    .hasFieldOrPropertyWithValue("name", null)
                    .hasFieldOrPropertyWithValue("type", null)
                    .hasFieldOrPropertyWithValue("id", null);
        }

        @Test
        @DisplayName("by a valid dto with special characters, should not change anythings and return a valid entity")
        void givenAValidDtoWithSpecialCharacters_whenMapToEntity_thenReturnAValidEntityWithoutAnyChange() {
            // given(Arrange) - precondition or setup:
            String name = "نام پروژه به همراه @#$%^&*()_+-=[]{}`~<>?/:;\"',.|\\£€¥₩§©®™✓\n";
            String type = "تایپ پروژه به همراه ¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹º»¼½¾¿×÷ßØÞþÆæÐð€ŒœŸŸ\n";
            ProjectDto input = ProjectDto.builder()
                    .publicId(projectId) // project_id has special validation
                    .name(name)
                    .type(type)
                    .build();

            // when(Act) - action or the behaviour that we are going test:
            ProjectEntity result = projectMapper.mapToEntity(input);

            // then(Assert) - verify the output:
            assertThat(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("publicId", projectId)
                    .hasFieldOrPropertyWithValue("name", name)
                    .hasFieldOrPropertyWithValue("type", type)
                    .hasFieldOrPropertyWithValue("id", null);
        }

        @Test
        @DisplayName("by a null dto, should throw NullPointerException")
        void givenAnInvalidNullDto_whenMapToEntity_thenThrowNullPointerException() {
            assertThatThrownBy(() -> projectMapper.mapToEntity(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("null");
        }
    }



}
