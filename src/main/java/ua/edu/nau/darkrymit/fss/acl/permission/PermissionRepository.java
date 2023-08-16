package ua.edu.nau.darkrymit.fss.acl.permission;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

  Optional<Permission> findByName(String name);
}
