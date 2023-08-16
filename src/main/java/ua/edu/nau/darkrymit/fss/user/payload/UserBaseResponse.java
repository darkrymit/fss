package ua.edu.nau.darkrymit.fss.user.payload;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UserBaseResponse {

  public static final String TYPE_NAME = "user";

  private UUID id;

  @Setter(AccessLevel.NONE)
  private String type = TYPE_NAME;

  private String email;

  private String name;

  private String firstName;

  private String lastName;
}
