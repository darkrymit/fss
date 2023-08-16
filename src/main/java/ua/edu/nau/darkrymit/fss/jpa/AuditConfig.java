package ua.edu.nau.darkrymit.fss.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import ua.edu.nau.darkrymit.fss.shared.SecurityUtils;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

  @Bean
  AuditorAware<UUID> idAuditingProvider() {
    return () -> Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
        .map(Jwt.class::cast).map(SecurityUtils::getUserId);
  }

}
