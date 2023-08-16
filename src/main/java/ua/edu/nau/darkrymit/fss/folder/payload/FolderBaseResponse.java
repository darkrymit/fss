package ua.edu.nau.darkrymit.fss.folder.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FolderBaseResponse {

  public static final String TYPE_NAME = "folder";

  private Long id;

  @Setter(AccessLevel.NONE)
  private String type = TYPE_NAME;

  private String name;

  private String description;
}
