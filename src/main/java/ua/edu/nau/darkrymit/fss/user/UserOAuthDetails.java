package ua.edu.nau.darkrymit.fss.user;

import java.beans.ConstructorProperties;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UserOAuthDetails {

  private final UUID id;
  private final String email;
  private final String firstName;
  private final String lastName;

  @ConstructorProperties({"id", "email", "firstName", "lastName"})
  public UserOAuthDetails(UUID id, String email, String firstName, String lastName) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
