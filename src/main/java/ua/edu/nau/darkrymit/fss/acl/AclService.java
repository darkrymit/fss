package ua.edu.nau.darkrymit.fss.acl;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccess;
import ua.edu.nau.darkrymit.fss.acl.file.UserFileAccessRepository;
import ua.edu.nau.darkrymit.fss.acl.folder.UserFolderAccess;
import ua.edu.nau.darkrymit.fss.acl.folder.UserFolderAccessRepository;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroupRepository;

@Service
@RequiredArgsConstructor
public class AclService {

  private final UserFileAccessRepository userFileAccessRepository;

  private final UserFolderAccessRepository userFolderAccessRepository;

  private final PermissionGroupRepository permissionGroupRepository;

  public boolean hasPermission(Authentication authentication, Long targetId, String targetType,
      String permission) {
    UUID userId = UUID.fromString(authentication.getName());
    if (!targetType.equals("file") && !targetType.equals("folder")) {
      throw new IllegalArgumentException("Target type must be file or folder");
    }
    PermissionGroup permissionGroup;
    if (targetType.equals("file")) {
      permissionGroup = getPermissionGroupForFile(userId, targetId);
    } else {
      permissionGroup = getPermissionGroupForFolder(userId, targetId);
    }
    if (permissionGroup == null) {
      throw new AccessDeniedException("Access denied");
    }
    return isGroupContainsPermission(permission, permissionGroup);

  }

  public void checkPermission(Authentication authentication, Long targetId, String targetType,
      String permission) throws AccessDeniedException {
    if (!hasPermission(authentication, targetId, targetType, permission)) {
      throw new AccessDeniedException("Access denied");
    }
  }

  @Transactional
  public void createFileAccess(UUID userUuid, Long fileId, String permissionGroupName) {
    UserFileAccess fileAccess = new UserFileAccess();
    fileAccess.setFileId(fileId);
    fileAccess.setUserUuid(userUuid);
    fileAccess.setPermissionGroup(
        permissionGroupRepository.findByName(permissionGroupName).orElseThrow());
    userFileAccessRepository.save(fileAccess);
  }

  @Transactional
  public void createFolderAccess(UUID userUuid, Long folderId, String permissionGroupName) {
    UserFolderAccess folderAccess = new UserFolderAccess();
    folderAccess.setFolderId(folderId);
    folderAccess.setUserUuid(userUuid);
    folderAccess.setPermissionGroup(
        permissionGroupRepository.findByName(permissionGroupName).orElseThrow());
    userFolderAccessRepository.save(folderAccess);
  }

  public void updateFileAccess(UUID userUuid, Long fileId, String permissionGroupName) {
    UserFileAccess fileAccess = userFileAccessRepository.findByFileIdAndUserUuid(fileId, userUuid)
        .orElseThrow();
    fileAccess.setPermissionGroup(
        permissionGroupRepository.findByName(permissionGroupName).orElseThrow());
    userFileAccessRepository.save(fileAccess);
  }

  public void updateFolderAccess(UUID userUuid, Long folderId, String permissionGroupName) {
    UserFolderAccess folderAccess = userFolderAccessRepository.findByFolderIdAndUserUuid(folderId,
        userUuid).orElseThrow();
    folderAccess.setPermissionGroup(
        permissionGroupRepository.findByName(permissionGroupName).orElseThrow());
    userFolderAccessRepository.save(folderAccess);
  }

  public void deleteFileAccess(UUID userUuid, Long fileId) {
    UserFileAccess fileAccess = userFileAccessRepository.findByFileIdAndUserUuid(fileId, userUuid)
        .orElseThrow();
    userFileAccessRepository.delete(fileAccess);
  }

  public void deleteFolderAccess(UUID userUuid, Long folderId) {
    UserFolderAccess folderAccess = userFolderAccessRepository.findByFolderIdAndUserUuid(folderId,
        userUuid).orElseThrow();
    userFolderAccessRepository.delete(folderAccess);
  }

  @Nullable
  public PermissionGroup getPermissionGroupForFile(UUID userUuid, Long fileId) {
    UserFileAccess fileAccess = userFileAccessRepository.findByFileIdAndUserUuid(fileId, userUuid)
        .orElse(null);
    if (fileAccess == null) {
      return null;
    }
    return fetchPermissionGroup(fileAccess.getPermissionGroup().getId());
  }

  @Nullable
  public PermissionGroup getPermissionGroupForFolder(UUID userUuid, Long folderId) {
    UserFolderAccess folderAccess = userFolderAccessRepository.findByFolderIdAndUserUuid(folderId,
        userUuid).orElse(null);
    if (folderAccess == null) {
      return null;
    }
    return fetchPermissionGroup(folderAccess.getPermissionGroup().getId());
  }

  private PermissionGroup fetchPermissionGroup(Integer id) {
    return permissionGroupRepository.findById(id).orElseThrow();
  }

  public boolean isGroupContainsPermission(String permission, PermissionGroup permissionGroup) {
    return permissionGroup.getPermissions().stream().anyMatch(p -> p.getName().equals(permission));
  }


}
