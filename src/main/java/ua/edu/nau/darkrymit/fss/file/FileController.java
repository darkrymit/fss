package ua.edu.nau.darkrymit.fss.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoStandardResponse;
import ua.edu.nau.darkrymit.fss.file.payload.FileGetContentResponse;
import ua.edu.nau.darkrymit.fss.file.payload.FileUploadRequest;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionMiniResponse;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionStandardResponse;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionUploadRequest;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

  private final FileService fileService;

  @GetMapping("/{id}")
  public FileInfoStandardResponse getById(@PathVariable Long id, Authentication authentication) {
    return fileService.findById(id,authentication);
  }

  @GetMapping("/{id}/versions/{versionId}")
  public FileVersionStandardResponse getByIdAndVersion(@PathVariable Long id,
      @PathVariable Long versionId) {
    return fileService.findByIdAndVersion(id, versionId);
  }

  @GetMapping("/{id}/versions")
  public List<FileVersionMiniResponse> getVersions(@PathVariable Long id) {
    return fileService.findAllVersions(id);
  }

  @GetMapping("/{id}/content")
  public ResponseEntity<Resource> getContent(@PathVariable Long id,
      @Nullable @RequestParam(value = "version", required = false) Long versionId,
      Authentication authentication) {
    FileGetContentResponse response = fileService.getContent(id, versionId, authentication);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentDisposition(ContentDisposition.builder("attachment")
        .filename(response.getName(), StandardCharsets.UTF_8).build());
    httpHeaders.setContentType(MediaType.parseMediaType(response.getContentType()));
    return ResponseEntity.ok().headers(httpHeaders).body(response.getResource());
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  FileInfoStandardResponse upload(@RequestPart("details") FileUploadRequest request,
      @RequestPart("file") MultipartFile file, Authentication authentication) throws IOException {
    log.info("file: {}", file.getOriginalFilename());
    return fileService.upload(request, file, authentication);
  }

  @PostMapping(path = "/{id}/versions", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  FileInfoStandardResponse uploadVersion(@PathVariable Long id,
      @RequestPart("details") FileVersionUploadRequest request,
      @RequestPart("file") MultipartFile file, Authentication authentication) throws IOException {
    return fileService.uploadVersion(id, request, file, authentication);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteFileById(@PathVariable Long id,
      Authentication authentication) {
    fileService.deleteById(id, authentication);
    return ResponseEntity.noContent().build();
  }
}
