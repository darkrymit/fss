package ua.edu.nau.darkrymit.fss.file.version;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;
import ua.edu.nau.darkrymit.fss.file.info.FileInfo;
import ua.edu.nau.darkrymit.fss.file.storage.FileStorageInfo;

@Entity
@Table(name = "fss_file_version")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileVersion {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 256)
  private String name;

  @Column(name = "description", length = 256)
  @Nullable
  private String description;

  @Column(name = "size", nullable = false)
  private Long size;

  @Column(nullable = false, length = 256)
  private String contentType;

  @Column(nullable = false, length = 160)
  private String sha1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "storage_id", nullable = false)
  private FileStorageInfo storage;

  @Column(name = "storage_details", nullable = false)
  private String storageDetails;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id", nullable = false)
  private FileInfo file;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private Instant createdAt;

  @Column(name = "created_by_uuid", nullable = false)
  @CreatedBy
  private UUID createdBy;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    FileVersion that = (FileVersion) o;
    return getId() != null && Objects.equals(getId(), that.getId());
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
        "size = " + size + ", " +
        "contentType = " + contentType + ", " +
        "sha1 = " + sha1 + ", " +
        "storageDetails = " + storageDetails + ", " +
        "createdAt = " + createdAt + ", " +
        "createdBy = " + createdBy + ")";
  }
}
