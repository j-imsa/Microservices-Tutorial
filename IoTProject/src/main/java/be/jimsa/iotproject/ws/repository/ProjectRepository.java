package be.jimsa.iotproject.ws.repository;

import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByNameAndType(String name, String type);
}
