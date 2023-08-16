package ua.edu.nau.darkrymit.fss.acl;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class AclPermissionEvaluator implements PermissionEvaluator {

  private final AclService aclService;

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return aclService.hasPermission(authentication,(Long) targetId, targetType, (String) permission);
  }
}
