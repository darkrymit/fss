package ua.edu.nau.darkrymit.fss.file.storage;

import java.io.InputStream;
import ua.edu.nau.darkrymit.fss.file.version.FileVersion;

public interface FileStorageService {

  FileVersion save(FileVersion version, InputStream fileInputStream);

  InputStream getFileInputStream(FileVersion version);

  boolean exists(FileVersion version);

  void delete(FileVersion version);
}
