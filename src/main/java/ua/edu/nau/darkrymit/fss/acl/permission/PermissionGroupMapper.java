package ua.edu.nau.darkrymit.fss.acl.permission;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;
import ua.edu.nau.darkrymit.fss.acl.permission.payload.PermissionGroupMiniResponse;

//@formatter:off
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
//@formatter:on
public interface PermissionGroupMapper {

  PermissionGroupMiniResponse toMiniResponse(PermissionGroup permissionGroup);

  List<String> toPermissionNames(List<Permission> permissions);

  @Nullable
  default String toPermissionName(@Nullable Permission permission){
    if(permission==null){
      return null;
    }
    return permission.getName();
  }
}
