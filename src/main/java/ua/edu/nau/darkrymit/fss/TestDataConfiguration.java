package ua.edu.nau.darkrymit.fss;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccess;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccessRepository;
import ua.edu.nau.darkrymit.fss.acl.folder.UserFolderAccess;
import ua.edu.nau.darkrymit.fss.acl.folder.UserFolderAccessRepository;
import ua.edu.nau.darkrymit.fss.acl.permission.Permission;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroupRepository;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionRepository;
import ua.edu.nau.darkrymit.fss.file.info.FileInfo;
import ua.edu.nau.darkrymit.fss.file.version.FileVersion;
import ua.edu.nau.darkrymit.fss.file.info.FileInfoRepository;
import ua.edu.nau.darkrymit.fss.file.version.FileVersionRepository;
import ua.edu.nau.darkrymit.fss.folder.Folder;
import ua.edu.nau.darkrymit.fss.folder.FolderRepository;
import ua.edu.nau.darkrymit.fss.file.storage.FileStorageInfo;
import ua.edu.nau.darkrymit.fss.file.storage.FileStorageInfoRepository;

@Configuration
public class TestDataConfiguration {

  @Bean
  public CommandLineRunner startData(FolderRepository folderRepository,
      FileInfoRepository fileInfoRepository, FileVersionRepository fileVersionRepository,
      FileStorageInfoRepository fileStorageInfoRepository,
      PermissionRepository permissionRepository,
      PermissionGroupRepository permissionGroupRepository,
      UserFileAccessRepository userFileAccessRepository,
      UserFolderAccessRepository userFolderAccessRepository) {
    return (args) -> {

      Permission readPermission = new Permission();
      readPermission.setName("read");
      permissionRepository.save(readPermission);
      Permission writePermission = new Permission();
      writePermission.setName("write");
      permissionRepository.save(writePermission);
      Permission deletePermission = new Permission();
      deletePermission.setName("delete");
      permissionRepository.save(deletePermission);
      Permission transferOwnerShipPermission = new Permission();
      transferOwnerShipPermission.setName("transfer_ownership");
      permissionRepository.save(transferOwnerShipPermission);

      PermissionGroup ownerPermissionGroup = new PermissionGroup();
      ownerPermissionGroup.setName("owner");
      ownerPermissionGroup.setPermissions(
          List.of(readPermission, writePermission, deletePermission, transferOwnerShipPermission));
      permissionGroupRepository.save(ownerPermissionGroup);

      PermissionGroup viewPermissionGroup = new PermissionGroup();
      viewPermissionGroup.setName("view");
      viewPermissionGroup.setPermissions(List.of(readPermission));
      permissionGroupRepository.save(viewPermissionGroup);

      PermissionGroup editPermissionGroup = new PermissionGroup();
      editPermissionGroup.setName("edit");
      editPermissionGroup.setPermissions(List.of(readPermission, writePermission));
      permissionGroupRepository.save(editPermissionGroup);

      PermissionGroup fullPermissionGroup = new PermissionGroup();
      fullPermissionGroup.setName("delete");
      fullPermissionGroup.setPermissions(
          List.of(readPermission, writePermission, deletePermission));
      permissionGroupRepository.save(fullPermissionGroup);

      UUID uuid = UUID.fromString("e80113ae-bfc8-4673-befd-732197da81cd");
      Folder folder = new Folder();
      folder.setName("test");
      folder.setParent(null);
      folder.setDescription("Test folder");
      folder.setOwner(uuid);
      folder.setCreatedAt(Instant.now());
      folder.setCreatedBy(uuid);
      folder.setLastModifiedAt(Instant.now());
      folder.setLastModifiedBy(uuid);
      folderRepository.save(folder);

      UserFolderAccess userFolderAccess = new UserFolderAccess();
      userFolderAccess.setFolderId(folder.getId());
      userFolderAccess.setUserUuid(uuid);
      userFolderAccess.setPermissionGroup(ownerPermissionGroup);
      userFolderAccessRepository.save(userFolderAccess);

      Folder folder2 = new Folder();
      folder2.setName("test2");
      folder2.setParent(folder);
      folder2.setDescription("Test folder 2");
      folder2.setOwner(uuid);
      folder2.setCreatedAt(Instant.now());
      folder2.setCreatedBy(uuid);
      folder2.setLastModifiedAt(Instant.now());
      folder2.setLastModifiedBy(uuid);
      folderRepository.save(folder2);

      UserFolderAccess userFolderAccess2 = new UserFolderAccess();
      userFolderAccess2.setFolderId(folder2.getId());
      userFolderAccess2.setUserUuid(uuid);
      userFolderAccess2.setPermissionGroup(ownerPermissionGroup);
      userFolderAccessRepository.save(userFolderAccess2);

      FileInfo fileInfo = new FileInfo();
      fileInfo.setName("test.txt");
      fileInfo.setParent(folder);
      fileInfo.setDescription("Test file");
      fileInfo.setOwner(uuid);
      fileInfo.setCreatedAt(Instant.now());
      fileInfo.setCreatedBy(uuid);
      fileInfo.setLastModifiedAt(Instant.now());
      fileInfo.setLastModifiedBy(uuid);
      fileInfoRepository.save(fileInfo);

      UserFileAccess userFileAccess = new UserFileAccess();
      userFileAccess.setFileId(fileInfo.getId());
      userFileAccess.setUserUuid(uuid);
      userFileAccess.setPermissionGroup(ownerPermissionGroup);
      userFileAccessRepository.save(userFileAccess);

      FileStorageInfo fileStorageInfo = new FileStorageInfo();
      fileStorageInfo.setName("local");
      fileStorageInfoRepository.save(fileStorageInfo);

      FileVersion fileVersion = new FileVersion();
      fileVersion.setFile(fileInfo);
      fileVersion.setContentType("text/plain");
      fileVersion.setName("test.txt");
      fileVersion.setDescription("Test file version");
      fileVersion.setSha1("test");
      fileVersion.setSize(100L);
      fileVersion.setStorage(fileStorageInfo);
      fileVersion.setStorageDetails("encoded_test");
      fileVersion.setCreatedAt(Instant.now());
      fileVersion.setCreatedBy(uuid);
      fileVersionRepository.save(fileVersion);

      fileInfo.setCurrentVersion(fileVersion);
      fileInfoRepository.save(fileInfo);

    };
  }
}
