package ua.edu.nau.darkrymit.fss.acl.folder;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccess;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccess.CompositeId;

@Repository
public interface UserFolderAccessRepository extends JpaRepository<UserFolderAccess, CompositeId> {

  Optional<UserFolderAccess> findByFolderIdAndUserUuid(Long folderId, UUID userUuid);
}
