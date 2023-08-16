package ua.edu.nau.darkrymit.fss.user;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.webclient")
@Value
public class KeycloakWebClientProperties {
    String url;

    String registrationId;
}