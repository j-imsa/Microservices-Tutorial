package be.jimsa.iotproject.repository;

import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import be.jimsa.iotproject.ws.repository.ProjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private String projectName;
    private String projectType;
    private String projectPublicId;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        projectRepository.deleteAll();

        projectName = "Alpha t1";
        projectType = "Super Lux";
        projectPublicId = "TA_qCZ5ugVmutiGhLIlFHmPE";
    }

    @Nested
    @DisplayName("test for Save")
    class SaveTests {
        @Test
        @DisplayName("testSaveProject_withValidData_shouldReturnSavedProject")
        void givenACorrectProjectObject_whenSave_thenReturnSavedProject() {

            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .name(projectName)
                    .type(projectType)
                    .build();

            ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);

            assertThat(savedProjectEntity.getId()).isNotNull();
            assertThat(savedProjectEntity.getId()).isPositive(); // > 0
            assertThat(savedProjectEntity.getPublicId()).isEqualTo(projectEntity.getPublicId());
            assertThat(savedProjectEntity.getName()).isEqualTo(projectEntity.getName());
            assertThat(savedProjectEntity.getType()).isEqualTo(projectEntity.getType());
            assertThat(projectRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("testSaveProject_withoutPublicId_shouldThrowException")
        void givenAWrongObjectWithoutPublicId_whenSave_thenThrowException() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .name(projectName)
                    .type(projectType)
                    .build();

            assertThatThrownBy(
                    () -> projectRepository.save(projectEntity)
            )
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("not-null property references");

            entityManager.clear();
            assertThat(projectRepository.count()).isZero();
        }

        @Test
        @DisplayName("testSaveProject_withoutName_shouldThrowException")
        void givenAWrongObjectWithoutName_whenSave_thenThrowException() {
            ProjectEntity projectEntity = ProjectEntity.builder()
                    .publicId(projectPublicId)
                    .type(projectType)
                    .build();

            assertThatThrownBy(
                    () -> projectRepository.save(projectEntity)
            )
                    .isInstanceOf(DataIntegrityViolationException.class)
                    .hasMessageContaining("not-null property references");

            entityManager.clear();
            assertThat(projectRepository.count()).isZero();
        }

        @Test
        @DisplayName("testSaveProject_withTwoSamePublicId_shouldThrowException")
        void givenTwoObjectsWithSamePublicId_whenSave_thenThrowException() {
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
