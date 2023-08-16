package ua.edu.nau.darkrymit.fss.file.info;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;
import ua.edu.nau.darkrymit.fss.file.version.FileVersion;
import ua.edu.nau.darkrymit.fss.folder.Folder;

/**
 *
 */
@Entity
@Table(name = "fss_file_info")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileInfo {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 256)
  private String name;

  @Column(name = "description", length = 256)
  @Nullable
  private String description;

  @Column(name = "owner_uuid", nullable = false)
  private UUID owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Folder parent;

  @OneToOne
  @JoinColumn(name = "current_version_id",nullable = true)
  @Nullable
  private FileVersion currentVersion;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private Instant createdAt;

  @Column(name = "created_by_uuid", nullable = false)
  @CreatedBy
  private UUID createdBy;

  @Column(name = "last_modified_at", nullable = false)
  @LastModifiedDate
  private Instant lastModifiedAt;

  @Column(name = "last_modified_by_uuid", nullable = false)
  @CreatedBy
  private UUID lastModifiedBy;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    FileInfo fileInfo = (FileInfo) o;
    return getId() != null && Objects.equals(getId(), fileInfo.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "id = " + id + ", " +
        "name = " + name + ", " +
        "description = " + description + ", " +
        "owner = " + owner + ", " +
        "currentVersion = " + currentVersion + ", " +
        "createdAt = " + createdAt + ", " +
        "createdBy = " + createdBy + ", " +
        "lastModifiedAt = " + lastModifiedAt + ", " +
        "lastModifiedBy = " + lastModifiedBy + ")";
  }
}
