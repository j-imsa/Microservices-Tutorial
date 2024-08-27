package be.jimsa.iotproject.repository;

import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import be.jimsa.iotproject.ws.repository.ProjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        projectRepository.deleteAll();
    }

    @Test
    @DisplayName("testSaveProject_withValidData_shouldReturnSavedProject")
    void givenACorrectProjectObject_whenSave_thenReturnSavedProject() {

        ProjectEntity projectEntity = ProjectEntity.builder()
                .publicId("TA_qCZ5ugVmutiGhLIlFHmPE")
                .name("Alpha t1")
                .type("Super Lux")
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
                .name("Alpha t1")
                .type("Super Lux")
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
                .publicId("TA_qCZ5ugVmutiGhLIlFHmPE")
                .type("Super Lux")
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
                .publicId("TA_qCZ5ugVmutiGhLIlFHmPE")
                .name("Alpha t1")
                .type("Super Lux")
                .build();

        ProjectEntity projectEntity2 = ProjectEntity.builder()
                .publicId("TA_qCZ5ugVmutiGhLIlFHmPE")
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
