package ua.edu.nau.darkrymit.fss.folder.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.nau.darkrymit.fss.shared.NodeDetail;

@Data
@EqualsAndHashCode(callSuper = true)
public class FolderMiniResponse extends FolderBaseResponse implements NodeDetail {

}
