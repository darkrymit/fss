package ua.edu.nau.darkrymit.fss.file.info;

import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroupMapper;
import ua.edu.nau.darkrymit.fss.acl.permission.payload.PermissionGroupMiniResponse;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoMiniResponse;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoStandardResponse;
import ua.edu.nau.darkrymit.fss.file.version.FileVersionMapper;
import ua.edu.nau.darkrymit.fss.folder.FolderMapper;
import ua.edu.nau.darkrymit.fss.shared.InstantMapper;
import ua.edu.nau.darkrymit.fss.user.UserUuidMapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {
    InstantMapper.class, FileVersionMapper.class, FolderMapper.class, UserUuidMapper.class,
    PermissionGroupMapper.class})
public interface FileInfoMapper {

  //if red colored, then it is probably a bug in the plugin
  @Mapping(target = "fileVersion", source = "fileInfo.currentVersion")
  @Mapping(target = "sha1", source = "fileInfo.currentVersion.sha1")
  @Mapping(target = "permissionGroup", source = "permissionGroup")
  @Mapping(target = "id",source = "fileInfo.id")
  @Mapping(target = "name",source = "fileInfo.name")
  FileInfoStandardResponse toStandardResponse(FileInfo fileInfo, PermissionGroup permissionGroup);

  @Mapping(target = "fileVersion", source = "currentVersion")
  @Mapping(target = "sha1", source = "currentVersion.sha1")
  FileInfoMiniResponse toMiniResponse(FileInfo fileInfo);

}
