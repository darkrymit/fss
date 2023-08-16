package ua.edu.nau.darkrymit.fss.folder;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderCreateRequest;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderStandardResponse;
import ua.edu.nau.darkrymit.fss.shared.SecurityUtils;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

  private final FolderService folderService;

  @GetMapping
  public List<FolderStandardResponse> getRoot(Authentication authentication) {
    return folderService.findAll(authentication);
  }

  @PostMapping
  public FolderStandardResponse createFolder(
      @Valid @RequestBody FolderCreateRequest createRequest, Authentication authentication) {
    return folderService.create(authentication, createRequest);
  }

  @GetMapping("/{id}")
  public FolderStandardResponse getFolderById(@PathVariable Long id,Authentication authentication) {
    return folderService.findById(id,authentication);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteFolderById(@PathVariable Long id,Authentication authentication) {
    folderService.deleteById(id,authentication);
    return ResponseEntity.noContent().build();
  }

}
