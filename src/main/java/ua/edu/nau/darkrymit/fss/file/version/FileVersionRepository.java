package ua.edu.nau.darkrymit.fss.file.version;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion, Integer> {

  Optional<FileVersion> findByFileIdAndId(Long id, Long versionId);


  List<FileVersion> findAllByFileId(Long id);

  void deleteAllByFileId(Long id);
}
