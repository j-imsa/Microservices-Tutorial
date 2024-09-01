package be.jimsa.iotproject.ws.repository;

import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByNameAndType(String name, String type);
    Optional<ProjectEntity> findByPublicId(String publicId);

    @Transactional
    @Modifying
    @Query("UPDATE ProjectEntity p SET p.name = ?1, p.type = ?2 WHERE p.publicId = ?3")
    int updateProjectEntity(String name, String type, String publicId);
}
