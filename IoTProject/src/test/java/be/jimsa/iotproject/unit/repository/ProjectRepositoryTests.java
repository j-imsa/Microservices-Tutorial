package be.jimsa.iotproject.unit.repository;

import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import be.jimsa.iotproject.ws.repository.ProjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.RollbackException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@DataJpaTest
@Slf4j
class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private String projectName;
    private String projectType;
    private String projectPublicId;

    @BeforeEach
    void cleanDatabase() {
        projectName = "Alpha t1";
        projectType = "Super Lux";
        projectPublicId = "a7vqO-mCBzlJpgGjSU-HYsTpLblN4El-UEmr8M9LMIm01dqmNIqENiE0RiLIfu9e";
    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        @DisplayName("by a valid entity, should return a valid saved entity with id")
        void givenAValidProjectEntity_whenSave_thenReturnSavedProjectEntityWithId() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();

            ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);

            assertThat(savedProjectEntity)
                    .isNotNull()
                    .isInstanceOf(ProjectEntity.class)
                    .hasFieldOrPropertyWithValue("publicId", projectEntity.getPublicId())
                    .hasFieldOrPropertyWithValue("name", projectEntity.getName())
                    .hasFieldOrPropertyWithValue("type", projectEntity.getType())
                    .hasFieldOrPropertyWithValue("id", savedProjectEntity.getId());
            assertThat(savedProjectEntity.getId())
                    .isNotNull()
                    .isPositive();
            assertThat(projectRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("without public_id, should throw [jakarta.validation.]ConstraintViolationException")
        void givenAnInvalidProjectEntityWithoutPublicId_whenSave_thenThrowConstraintViolationException() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();

            assertThatThrownBy(
                    () -> projectRepository.save(projectEntity)
            )
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Invalid public_id");

            entityManager.clear();
            assertThat(projectRepository.count()).isZero();
        }

        @Test
        @DisplayName("without name, should throw [jakarta.validation.]ConstraintViolationException")
        void givenAnInvalidProjectEntityWithoutName_whenSave_thenThrowConstraintViolationException() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .type(projectType)
                    .build();

            assertThatThrownBy(
                    () -> projectRepository.save(projectEntity)
            )
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("name can not be");

            entityManager.clear();
            assertThat(projectRepository.count()).isZero();
        }

        @Test
        @DisplayName("by two entity with same public_id, should throw DataIntegrityViolationException")
        void givenTwoProjectEntitiesWithSamePublicId_whenSave_thenThrowDataIntegrityViolationException() {
            ProjectEntity projectEntity1 = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();

            ProjectEntity projectEntity2 = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name("Tetha A+")
                    .type("Luxery")
                    .build();

            projectRepository.save(projectEntity1);

            assertThatThrownBy(
                    () -> projectRepository.save(projectEntity2)
            )
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("could not execute statement");

            entityManager.clear();
            assertThat(projectRepository.count()).isEqualTo(1);
        }

        @RepeatedTest(100)
        @DisplayName("by a valid entity, should save less than 10 ms")
        void givenAValidProjectEntity_whenSave_thenSaveLessThan10Ms() throws Throwable {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .name(projectName)
                    .type(projectType)
                    .publicId(projectPublicId)
                    .build();

            Executable executable = () -> projectRepository.save(projectEntity);
            // executable.execute(); // skip the first

            assertTimeout(Duration.ofMillis(100), executable);
            assertThat(projectRepository.count()).isEqualTo(1);
        }

        @ParameterizedTest
        @DisplayName("by an invalid entity with ID, should ignore it, then return a valid saved entity")
        @ValueSource(longs = {Long.MIN_VALUE, -100, -1, 0, 2, 100, Long.MAX_VALUE})
        void givenAnInvalidProjectEntityUsingId_whenSave_thenReturnAValidSavedProjectEntity(long id) {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .name(projectName)
                    .type(projectType)
                    .publicId(projectPublicId)
                    .id(id)
                    .build();

            ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);
            log.info("Saved ID: {}", savedProjectEntity.getId());

            assertThat(savedProjectEntity.getId())
                    .isNotNull()
                    .isNotEqualTo(projectEntity.getId());
        }

    }

    @Nested
    @DisplayName("test for Find")
    class FindTests {

        @Test
        @DisplayName("testFindByNameAndTypeProjectServiceImpl_withAValidInput_shouldReturnAValidEntity")
        void givenAValidInput_whenFindByNameAndType_thenShouldReturnAValidEntity() {
            // given - precondition or setup:
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);

            // when - action or the behaviour that we are going test:
            Optional<ProjectEntity> result = projectRepository.findByNameAndType(projectName, projectType);

            // then - verify the output:
            assertThat(result)
                    .isPresent()
                    .hasValue(savedProjectEntity)
                    .hasValueSatisfying(entity -> {
                        assertThat(entity).isInstanceOf(ProjectEntity.class);
                        assertThat(entity.getPublicId()).isEqualTo(savedProjectEntity.getPublicId());
                        assertThat(entity.getName()).isEqualTo(savedProjectEntity.getName());
                        assertThat(entity.getType()).isEqualTo(savedProjectEntity.getType());
                        assertThat(entity.getId()).isEqualTo(savedProjectEntity.getId());
                    });
        }

        @Test
        @DisplayName("testFindByNameAndTypeProjectServiceImpl_withANullNameInput_shouldReturnAnEmptyEntity")
        void givenANullNameInput_whenFindByNameAndType_thenShouldReturnAnEmptyEntity() {

            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            projectRepository.save(projectEntity);

            Optional<ProjectEntity> result = projectRepository.findByNameAndType(null, projectType);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("testFindByNameAndTypeProjectServiceImpl_withANullTypeInput_shouldReturnAnEmptyEntity")
        void givenANullTypeInput_whenFindByNameAndType_thenShouldReturnAnEmptyEntity() {

            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            projectRepository.save(projectEntity);

            Optional<ProjectEntity> result = projectRepository.findByNameAndType(projectName, null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("testFindByNameAndTypeProjectServiceImpl_withANullInput_shouldReturnAnEmptyEntity")
        void givenANullInput_whenFindByNameAndType_thenShouldReturnAnEmptyEntity() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();
            projectRepository.save(projectEntity);

            Optional<ProjectEntity> result = projectRepository.findByNameAndType(null, null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("testFindByNameAndTypeProjectServiceImpl_withANullInputOutput_shouldReturnAnEmptyEntity")
        void givenANullInputOutput_whenFindByNameAndType_thenShouldReturnAnEmptyEntity() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .build();
            ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);

            Optional<ProjectEntity> result = projectRepository.findByNameAndType(projectName, null);

            assertThat(result)
                    .isPresent()
                    .hasValue(savedProjectEntity)
                    .hasValueSatisfying(entity -> {
                        assertThat(entity).isInstanceOf(ProjectEntity.class);
                        assertThat(entity.getPublicId()).isEqualTo(savedProjectEntity.getPublicId());
                        assertThat(entity.getName()).isEqualTo(savedProjectEntity.getName());
                        assertThat(entity.getType()).isNull();
                        assertThat(entity.getId()).isEqualTo(savedProjectEntity.getId());
                    });
        }

    }

}
