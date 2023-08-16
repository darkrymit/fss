package ua.edu.nau.darkrymit.fss.folder;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

  List<Folder> findAllByParentId(Long id);

  List<Folder> findByOwnerAndParentIdNull(UUID owner);

}
