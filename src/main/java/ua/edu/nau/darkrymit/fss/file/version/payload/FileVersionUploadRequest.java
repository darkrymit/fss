package ua.edu.nau.darkrymit.fss.file.version.payload;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class FileVersionUploadRequest {

  @Nullable
  String name;

  @Nullable
  String description;

  @Nullable
  Integer storageId;

}
