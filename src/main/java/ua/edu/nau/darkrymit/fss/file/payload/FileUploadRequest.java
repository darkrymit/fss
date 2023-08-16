package ua.edu.nau.darkrymit.fss.file.payload;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class FileUploadRequest {

  Long parentId;

  @Nullable
  String name;

  @Nullable
  String description;

  @Nullable
  String versionDescription;

  @Nullable
  Integer storageId;

}
