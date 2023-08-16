package ua.edu.nau.darkrymit.fss.file.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.edu.nau.darkrymit.fss.file.version.FileVersion;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnDiskFileStorageService implements FileStorageService {

  private final OnDiskFileStorageProperties configurationProperties;

  @Override
  public FileVersion save(FileVersion version, InputStream fileInputStream) {
    try {
      String prefix = "encrypt_";
      Path tempFile = Files.createTempFile(configurationProperties.path(), prefix, "");
      fileInputStream.transferTo(Files.newOutputStream(tempFile));
      version.setStorageDetails(tempFile.getFileName().toString());
      return version;
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  @Override
  public InputStream getFileInputStream(FileVersion version) {
    try {
      Path target = configurationProperties.path().resolve(version.getStorageDetails());
      return Files.newInputStream(target);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  @Override
  public boolean exists(FileVersion version) {
    Path target = configurationProperties.path().resolve(version.getStorageDetails());
    return Files.exists(target);
  }

  @Override
  public void delete(FileVersion version) {
    try {
      Path target = configurationProperties.path().resolve(version.getStorageDetails());
      Files.delete(target);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }
}
