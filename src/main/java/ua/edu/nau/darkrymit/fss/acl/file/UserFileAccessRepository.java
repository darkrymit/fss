package ua.edu.nau.darkrymit.fss.acl.file;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccess.CompositeId;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;

@Repository
public interface UserFileAccessRepository extends JpaRepository<UserFileAccess, CompositeId> {
  Optional<UserFileAccess> findByFileIdAndUserUuid(Long fileId, UUID userUuid);
}
