package ua.edu.nau.darkrymit.fss.folder.payload;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.nau.darkrymit.fss.acl.permission.payload.PermissionGroupMiniResponse;
import ua.edu.nau.darkrymit.fss.shared.NodeDetail;
import ua.edu.nau.darkrymit.fss.user.payload.UserMiniResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class FolderStandardResponse extends FolderBaseResponse {

  List<NodeDetail> children;

  private Long createdAt;

  private UserMiniResponse createdBy;

  private Long lastModifiedAt;

  private UserMiniResponse lastModifiedBy;

  private UserMiniResponse owner;

  private FolderMiniResponse parent;

  private PermissionGroupMiniResponse permissionGroup;
}
