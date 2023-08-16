package ua.edu.nau.darkrymit.fss.acl.permission.payload;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderBaseResponse;
import ua.edu.nau.darkrymit.fss.shared.NodeDetail;

@Data
public class PermissionGroupMiniResponse {

  private String name;

  private List<String> permissions;
}
