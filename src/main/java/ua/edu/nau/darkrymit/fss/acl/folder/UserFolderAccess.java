package ua.edu.nau.darkrymit.fss.acl.folder;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import ua.edu.nau.darkrymit.fss.acl.folder.UserFolderAccess.CompositeId;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.folder.Folder;

@Entity
@Table(name = "fss_acl_user_folder_access")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(CompositeId.class)
public class UserFolderAccess {

  @Id
  private Long folderId;

  @Id
  private UUID userUuid;

  @ManyToOne(fetch = FetchType.LAZY)
  private PermissionGroup permissionGroup;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CompositeId implements Serializable {

    private Long folderId;
    private UUID userUuid;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    UserFolderAccess that = (UserFolderAccess) o;
    return getFolderId() != null && Objects.equals(getFolderId(), that.getFolderId())
        && getUserUuid() != null && Objects.equals(getUserUuid(), that.getUserUuid());
  }

  @Override
  public int hashCode() {
    return Objects.hash(folderId, userUuid);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "folderId = " + folderId + ", " +
        "userUuid = " + userUuid + ")";
  }
}
