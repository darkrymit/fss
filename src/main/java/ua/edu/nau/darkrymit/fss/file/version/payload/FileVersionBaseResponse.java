package ua.edu.nau.darkrymit.fss.file.version.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class FileVersionBaseResponse {

  public static final String TYPE_NAME = "fileVersion";

  private Long id;

  @Setter(AccessLevel.NONE)
  private String type = TYPE_NAME;

  private String name;

  private String contentType;

  private Long size;

  private String sha1;

}
