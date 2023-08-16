package ua.edu.nau.darkrymit.fss.file;

import jakarta.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.nau.darkrymit.fss.acl.AclService;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.file.info.FileInfo;
import ua.edu.nau.darkrymit.fss.file.info.FileInfoMapper;
import ua.edu.nau.darkrymit.fss.file.info.FileInfoRepository;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoMiniResponse;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoStandardResponse;
import ua.edu.nau.darkrymit.fss.file.payload.FileGetContentResponse;
import ua.edu.nau.darkrymit.fss.file.payload.FileUploadRequest;
import ua.edu.nau.darkrymit.fss.file.storage.FileStorageInfo;
import ua.edu.nau.darkrymit.fss.file.storage.FileStorageInfoRepository;
import ua.edu.nau.darkrymit.fss.file.storage.FileStorageService;
import ua.edu.nau.darkrymit.fss.file.version.FileVersion;
import ua.edu.nau.darkrymit.fss.file.version.FileVersionMapper;
import ua.edu.nau.darkrymit.fss.file.version.FileVersionRepository;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionMiniResponse;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionStandardResponse;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionUploadRequest;
import ua.edu.nau.darkrymit.fss.folder.FolderService;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderMiniResponse;
import ua.edu.nau.darkrymit.fss.shared.SecurityUtils;

@Service
public class FileService {

  private final FileInfoRepository fileInfoRepository;

  private final FileVersionRepository fileVersionRepository;

  private final FileInfoMapper fileInfoMapper;

  private final FileVersionMapper fileVersionMapper;

  private final FileStorageService fileStorageService;

  private final FileStorageInfoRepository fileStorageInfoRepository;

  private final AclService aclService;

  private final MimeTypes mimeTypes;

  // autowired by method to avoid circular dependency
  private FolderService folderService;

  public FileService(FileInfoRepository fileInfoRepository,
      FileVersionRepository fileVersionRepository, FileInfoMapper fileInfoMapper,
      FileVersionMapper fileVersionMapper, FileStorageService fileStorageService,
      FileStorageInfoRepository fileStorageInfoRepository, AclService aclService) {
    this.fileInfoRepository = fileInfoRepository;
    this.fileVersionRepository = fileVersionRepository;
    this.fileInfoMapper = fileInfoMapper;
    this.fileVersionMapper = fileVersionMapper;
    this.fileStorageService = fileStorageService;
    this.fileStorageInfoRepository = fileStorageInfoRepository;
    this.aclService = aclService;
    TikaConfig config = TikaConfig.getDefaultConfig();
    this.mimeTypes = config.getMimeRepository();
  }


  private static String getSha1(DigestInputStream digestStream) {
    return Hex.encodeHexString(digestStream.getMessageDigest().digest());
  }

  private static DigestInputStream getDigestStream(InputStream dataStream, String algorithm) {
    var digest = DigestUtils.getDigest(algorithm);
    return new DigestInputStream(dataStream, digest);
  }

  private static String getFileName(@Nullable String providedName, MultipartFile file) {
    String name = providedName;
    if (name == null) {
      name = file.getOriginalFilename();
    }
    Objects.requireNonNull(name);
    return name;
  }

  @Autowired
  public void setFolderService(FolderService folderService) {
    this.folderService = folderService;
  }

  @PostAuthorize("hasPermission(#id, 'file', 'read')")
  public FileInfoStandardResponse findById(Long id,Authentication authentication) {
    UUID userId = SecurityUtils.getUserId(authentication);
    FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow();
    PermissionGroup permissionGroup = aclService.getPermissionGroupForFile(userId,
        fileInfo.getId());
    Objects.requireNonNull(permissionGroup);
    return fileInfoMapper.toStandardResponse(fileInfo, permissionGroup);
  }

  public List<FileInfoMiniResponse> findAllByParentId(Long folderId) {
    return fileInfoRepository.findAllByParentId(folderId).stream()
        .map(fileInfoMapper::toMiniResponse).toList();
  }

  @PostAuthorize("hasPermission(#id, 'file', 'read')")
  public FileVersionStandardResponse findByIdAndVersion(Long id, Long versionId) {
    FileVersion fileVersion = fileVersionRepository.findByFileIdAndId(id, versionId).orElseThrow();
    return fileVersionMapper.toStandardResponse(fileVersion);
  }

  @PostAuthorize("hasPermission(#id, 'file', 'read')")
  public List<FileVersionMiniResponse> findAllVersions(Long id) {
    return fileVersionRepository.findAllByFileId(id).stream().map(fileVersionMapper::toMiniResponse)
        .toList();
  }

  @Transactional
  public void deleteById(Long id, Authentication authentication) {
    FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow();

    aclService.checkPermission(authentication, fileInfo.getId(), "file", "delete");
    // unlink current version to avoid constraint violation
    fileInfo.setCurrentVersion(null);

    fileInfoRepository.save(fileInfo);

    List<FileVersion> fileVersions = fileVersionRepository.findAllByFileId(id);

    for (FileVersion fileVersion : fileVersions) {
      fileStorageService.delete(fileVersion);
      fileVersionRepository.delete(fileVersion);
    }
    fileInfoRepository.delete(fileInfo);
  }

  @Transactional
  public FileInfoStandardResponse upload(FileUploadRequest request, MultipartFile file,
      Authentication authentication) throws IOException {
    UUID userId = SecurityUtils.getUserId(authentication);

    FolderMiniResponse parent = folderService.findMiniById(request.getParentId());
    aclService.checkPermission(authentication, parent.getId(), "folder", "write");

    FileInfo fileInfo = new FileInfo();
    fileInfo.setName(getFileName(request.getName(), file));
    fileInfo.setDescription(request.getDescription());
    fileInfo.setParent(folderService.getReferenceById(parent.getId()));
    fileInfo.setOwner(userId);

    Integer storageId = Optional.ofNullable(request.getStorageId()).orElse(1);
    FileStorageInfo fileStorageInfo = fileStorageInfoRepository.findById(storageId).orElseThrow();

    FileVersion fileVersion = new FileVersion();
    fileVersion.setFile(fileInfo);
    fileVersion.setName(file.getName());
    fileVersion.setDescription(request.getVersionDescription());
    fileVersion.setContentType(Objects.requireNonNull(file.getContentType()));
    fileVersion.setStorage(fileStorageInfo);

    fileInfo.setCurrentVersion(fileVersion);

    saveFileVersion(file, fileVersion);

    fileInfoRepository.save(fileInfo);
    fileVersionRepository.save(fileVersion);

    aclService.createFileAccess(userId, fileInfo.getId(), "owner");
    PermissionGroup permissionGroup = aclService.getPermissionGroupForFile(userId,
        fileInfo.getId());
    Objects.requireNonNull(permissionGroup);
    return fileInfoMapper.toStandardResponse(fileInfo, permissionGroup);
  }

  @Transactional
  public FileInfoStandardResponse uploadVersion(Long id, FileVersionUploadRequest request,
      MultipartFile file, Authentication authentication) throws IOException {
    FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow();
    UUID userId = SecurityUtils.getUserId(authentication);
    aclService.checkPermission(authentication, fileInfo.getId(), "file", "write");

    FileVersion fileVersion = new FileVersion();
    fileVersion.setFile(fileInfo);
    fileVersion.setName(getFileName(request.getName(), file));
    fileVersion.setDescription(request.getDescription());
    fileVersion.setContentType(Objects.requireNonNull(file.getContentType()));

    Integer storageId = Optional.ofNullable(request.getStorageId()).orElse(1);
    FileStorageInfo fileStorageInfo = fileStorageInfoRepository.findById(storageId).orElseThrow();
    fileVersion.setStorage(fileStorageInfo);

    saveFileVersion(file, fileVersion);

    fileVersionRepository.save(fileVersion);
    fileInfo.setCurrentVersion(fileVersion);
    fileInfoRepository.save(fileInfo);
    PermissionGroup permissionGroup = aclService.getPermissionGroupForFile(userId,
        fileInfo.getId());
    Objects.requireNonNull(permissionGroup);
    return fileInfoMapper.toStandardResponse(fileInfo, permissionGroup);
  }

  public FileGetContentResponse getContent(Long fileId, @Nullable Long versionId,
      Authentication authentication) {
    FileInfo fileInfo = fileInfoRepository.findById(fileId).orElseThrow();
    aclService.checkPermission(authentication, fileInfo.getId(), "file", "read");
    FileVersion fileVersion = Optional.ofNullable(versionId)
        .map(id -> fileVersionRepository.findByFileIdAndId(fileId, id).orElseThrow())
        .orElse(fileInfo.getCurrentVersion());
    Objects.requireNonNull(fileVersion);
    String name = getFileNameForMimeType(fileInfo.getName(), fileVersion.getContentType());
    InputStream stream = fileStorageService.getFileInputStream(fileVersion);
    Resource resource = new InputStreamResource(stream);
    return new FileGetContentResponse(name, fileVersion.getContentType(), resource);
  }

  private void saveFileVersion(MultipartFile file, FileVersion fileVersion) throws IOException {
    InputStream dataStream = file.getInputStream();
    dataStream = new BufferedInputStream(dataStream);

    var digestStream = getDigestStream(dataStream, "SHA-1");
    dataStream = digestStream;

    var countingStream = new CountingInputStream(dataStream);
    dataStream = countingStream;

    fileStorageService.save(fileVersion, dataStream);

    fileVersion.setSize(countingStream.getByteCount());
    fileVersion.setSha1(getSha1(digestStream));
  }

  private String getFileNameForMimeType(String originalName, String mimeType) {
    try {
      String possibleExtension = mimeTypes.forName(mimeType).getExtension();
      return originalName.endsWith(possibleExtension) ? originalName
          : originalName + possibleExtension;
    } catch (MimeTypeException e) {
      return originalName;
    }
  }
}
