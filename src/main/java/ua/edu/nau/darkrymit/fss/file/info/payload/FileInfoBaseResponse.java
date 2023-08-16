package ua.edu.nau.darkrymit.fss.file.info.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionMiniResponse;

@Getter
@Setter
public abstract class FileInfoBaseResponse {

  public static final String TYPE_NAME = "file";

  private Long id;

  @Setter(AccessLevel.NONE)
  private String type = TYPE_NAME;

  private String name;

  private String description;

  private String sha1;

  private FileVersionMiniResponse fileVersion;

}
