package ua.edu.nau.darkrymit.fss.file.info;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.edu.nau.darkrymit.fss.file.info.FileInfo;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

  List<FileInfo> findAllByParentId(Long id);
}
