package ua.edu.nau.darkrymit.fss.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import ua.edu.nau.darkrymit.fss.acl.AclPermissionEvaluator;
import ua.edu.nau.darkrymit.fss.acl.AclService;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

  @Bean
  public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(
      AclService aclService) {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    var permissionEvaluator = new AclPermissionEvaluator(aclService);
    expressionHandler.setPermissionEvaluator(permissionEvaluator);
    return expressionHandler;
  }
}
