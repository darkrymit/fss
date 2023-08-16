package ua.edu.nau.darkrymit.fss.folder;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.edu.nau.darkrymit.fss.acl.AclService;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.file.FileService;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoBaseResponse;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderCreateRequest;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderMiniResponse;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderStandardResponse;
import ua.edu.nau.darkrymit.fss.shared.SecurityUtils;

@Service
@RequiredArgsConstructor
public class FolderService {

  private final FolderRepository folderRepository;

  private final FolderMapper folderMapper;

  private final AclService aclService;

  // autowired by method to avoid circular dependency
  private FileService fileService;

  public List<FolderStandardResponse> findAll(Authentication authentication) {
    UUID userId = SecurityUtils.getUserId(authentication);
    List<Folder> folders = folderRepository.findByOwnerAndParentIdNull(userId);
    return folders.stream()
        .map(folder -> toFolderStandardResponse(folder, userId)).toList();
  }

  private FolderStandardResponse toFolderStandardResponse(Folder folder, UUID userId) {
    PermissionGroup permissionGroup = aclService.getPermissionGroupForFolder(userId,
        folder.getId());
    Objects.requireNonNull(permissionGroup);
    return folderMapper.toStandardResponse(folder, permissionGroup, findAllByParent(folder),
        fileService.findAllByParentId(folder.getId()));
  }

  public List<FolderMiniResponse> findAllByParent(Folder folder) {
    return folderRepository.findAllByParentId(folder.getId()).stream()
        .map(folderMapper::toMiniResponse).toList();
  }

  @PreAuthorize("hasPermission(#id, 'folder', 'read')")
  public FolderStandardResponse findById(Long id, Authentication authentication) {
    Folder folder = folderRepository.findById(id).orElseThrow();
    return toFolderStandardResponse(folder, SecurityUtils.getUserId(authentication));
  }

  @PreAuthorize("hasPermission(#id, 'folder', 'read')")
  public FolderMiniResponse findMiniById(Long id) {
    Folder folder = folderRepository.findById(id).orElseThrow();
    return folderMapper.toMiniResponse(folder);
  }

  public Folder getReferenceById(Long id) {
    return folderRepository.getReferenceById(id);
  }

  @Transactional
  public FolderStandardResponse create(Authentication authentication,
      FolderCreateRequest createRequest) {
    UUID userId = SecurityUtils.getUserId(authentication);
    Folder folder = new Folder();
    folder.setName(createRequest.getName());
    folder.setDescription(createRequest.getDescription());
    Folder parent = null;
    Long parentId = createRequest.getParentId();
    if (parentId != null) {
      parent = folderRepository.findById(parentId).orElseThrow();
      aclService.checkPermission(authentication, parent.getId(), "folder", "write");
    }
    folder.setParent(parent);
    folder.setOwner(userId);
    folderRepository.save(folder);
    aclService.createFolderAccess(userId, folder.getId(), "owner");
    return toFolderStandardResponse(folder, userId);
  }

  @Transactional
  public void deleteById(Long id, Authentication authentication) {
    Folder folder = folderRepository.findById(id).orElseThrow();
    aclService.checkPermission(authentication, folder.getId(), "folder", "delete");
    List<Long> fileIds = fileService.findAllByParentId(folder.getId()).stream()
        .map(FileInfoBaseResponse::getId).toList();
    for (Long fileId : fileIds) {
      fileService.deleteById(fileId, authentication);
    }
    List<Folder> folders = folderRepository.findAllByParentId(id);
    for (Folder childFolder : folders) {
      deleteById(childFolder.getId(), authentication);
    }
    folderRepository.deleteById(folder.getId());
  }

  @Autowired
  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }
}
