package ua.edu.nau.darkrymit.fss.file.info.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.nau.darkrymit.fss.acl.permission.payload.PermissionGroupMiniResponse;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderMiniResponse;
import ua.edu.nau.darkrymit.fss.user.payload.UserMiniResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileInfoStandardResponse extends FileInfoBaseResponse {

  private Long createdAt;

  private UserMiniResponse createdBy;

  private Long lastModifiedAt;

  private UserMiniResponse lastModifiedBy;

  private UserMiniResponse owner;

  private FolderMiniResponse parent;

  private PermissionGroupMiniResponse permissionGroup;
}
