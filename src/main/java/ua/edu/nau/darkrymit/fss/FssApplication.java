package ua.edu.nau.darkrymit.fss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.edu.nau.darkrymit.fss.file.storage.OnDiskFileStorageProperties;
import ua.edu.nau.darkrymit.fss.user.KeycloakWebClientProperties;

@SpringBootApplication
@EnableConfigurationProperties({KeycloakWebClientProperties.class,
    OnDiskFileStorageProperties.class})
public class FssApplication {

  public static void main(String[] args) {
    SpringApplication.run(FssApplication.class, args);
  }

}
