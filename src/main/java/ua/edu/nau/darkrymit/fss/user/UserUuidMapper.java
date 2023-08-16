package ua.edu.nau.darkrymit.fss.user;

import java.util.Objects;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ua.edu.nau.darkrymit.fss.user.payload.UserMiniResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserUuidMapper {

  @Nullable
  protected UserService userService;

  public UserMiniResponse toMiniResponse(UUID uuid) {
    Objects.requireNonNull(userService);
    return userService.getByIdMini(uuid);
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
