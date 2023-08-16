package ua.edu.nau.darkrymit.fss.folder.payload;

import java.beans.ConstructorProperties;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class FolderCreateRequest {

  String name;

  String description;

  @Nullable
  Long parentId;

  @ConstructorProperties({"name", "description", "parentId"})
  public FolderCreateRequest(String name, String description,@Nullable Long parentId) {
    this.name = name;
    this.description = description;
    this.parentId = parentId;
  }
}
