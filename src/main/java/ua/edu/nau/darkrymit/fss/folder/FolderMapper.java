package ua.edu.nau.darkrymit.fss.folder;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroup;
import ua.edu.nau.darkrymit.fss.acl.permission.PermissionGroupMapper;
import ua.edu.nau.darkrymit.fss.acl.permission.payload.PermissionGroupMiniResponse;
import ua.edu.nau.darkrymit.fss.file.info.FileInfoMapper;
import ua.edu.nau.darkrymit.fss.file.info.payload.FileInfoMiniResponse;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderMiniResponse;
import ua.edu.nau.darkrymit.fss.folder.payload.FolderStandardResponse;
import ua.edu.nau.darkrymit.fss.shared.InstantMapper;
import ua.edu.nau.darkrymit.fss.shared.NodeDetail;
import ua.edu.nau.darkrymit.fss.user.UserUuidMapper;

//@formatter:off
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {InstantMapper.class, FileInfoMapper.class, UserUuidMapper.class,PermissionGroupMapper.class}
)
//@formatter:on
public interface FolderMapper {

  //if red colored, then it is probably a bug in the plugin
  @Mapping(target = "children", expression = "java(toNodeDetails(folderChildren,fileChildren))")
  @Mapping(target = "permissionGroup", source = "permissionGroup")
  @Mapping(target = "id",source = "folder.id")
  @Mapping(target = "name",source = "folder.name")
  FolderStandardResponse toStandardResponse(Folder folder,PermissionGroup permissionGroup,
      @Context List<FolderMiniResponse> folderChildren,@Context List<FileInfoMiniResponse> fileChildren);

  FolderMiniResponse toMiniResponse(Folder folder);

  default List<NodeDetail> toNodeDetails(List<FolderMiniResponse> folderChildren,
      List<FileInfoMiniResponse> fileChildren) {
    List<NodeDetail> nodeDetails = new ArrayList<>();
    nodeDetails.addAll(folderChildren);
    nodeDetails.addAll(fileChildren);
    return nodeDetails;
  }

}
