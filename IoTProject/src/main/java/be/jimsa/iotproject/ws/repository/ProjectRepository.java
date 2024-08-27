package be.jimsa.iotproject.ws.repository;

import be.jimsa.iotproject.ws.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
