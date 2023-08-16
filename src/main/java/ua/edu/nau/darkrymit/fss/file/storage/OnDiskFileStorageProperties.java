package ua.edu.nau.darkrymit.fss.file.storage;

import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file-storage.on-disk")
public record OnDiskFileStorageProperties(Path path) {

}