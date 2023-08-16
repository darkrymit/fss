package ua.edu.nau.darkrymit.fss.acl.permission;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {

  @EntityGraph(attributePaths = {"permissions"})
  Optional<PermissionGroup> findByName(String name);

  @EntityGraph(attributePaths = {"permissions"})
  Optional<PermissionGroup> findById(Integer id);
}
