package ua.edu.nau.darkrymit.fss.acl.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccess.CompositeId;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;

@Entity
@Table(name = "fss_acl_user_file_access")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(CompositeId.class)
public class UserFileAccess {

  @Id
  private Long fileId;

  @Id
  private UUID userUuid;

  @ManyToOne(fetch = FetchType.LAZY)
  private PermissionGroup permissionGroup;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CompositeId implements Serializable {
    private Long fileId;
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
    UserFileAccess that = (UserFileAccess) o;
    return getFileId() != null && Objects.equals(getFileId(), that.getFileId())
        && getUserUuid() != null && Objects.equals(getUserUuid(), that.getUserUuid());
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileId, userUuid);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "fileId = " + fileId + ", " +
        "userUuid = " + userUuid + ")";
  }
}
