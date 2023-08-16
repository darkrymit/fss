package ua.edu.nau.darkrymit.fss.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.nau.darkrymit.fss.shared.InstantMapper;
import ua.edu.nau.darkrymit.fss.user.payload.UserMiniResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {
    InstantMapper.class})
public interface UserOAuthMapper {


  @Mapping(target = "name", expression = "java(userOAuthDetails.getFirstName() + \" \" + userOAuthDetails.getLastName())")
  UserMiniResponse toMiniResponse(UserOAuthDetails userOAuthDetails);

}
