package ua.edu.nau.darkrymit.fss.shared;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class SecurityUtils {

  private SecurityUtils() {
  }

  public static UUID getUserId(Jwt jwt) {
    return UUID.fromString(jwt.getSubject());
  }

  public static UUID getUserId(Authentication authentication) {
    return getUserId((Jwt) authentication.getPrincipal());
  }
}
