package ua.edu.nau.darkrymit.fss.file.version;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionMiniResponse;
import ua.edu.nau.darkrymit.fss.file.version.payload.FileVersionStandardResponse;
import ua.edu.nau.darkrymit.fss.shared.InstantMapper;
import ua.edu.nau.darkrymit.fss.user.UserUuidMapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {
    InstantMapper.class, UserUuidMapper.class})
public interface FileVersionMapper {

  FileVersionMiniResponse toMiniResponse(FileVersion fileVersion);

  FileVersionStandardResponse toStandardResponse(FileVersion fileVersion);
}
