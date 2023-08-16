package ua.edu.nau.darkrymit.fss.file.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStorageInfoRepository extends JpaRepository<FileStorageInfo, Integer> {


}
