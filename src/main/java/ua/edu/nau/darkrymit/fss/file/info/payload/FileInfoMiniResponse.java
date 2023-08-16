package ua.edu.nau.darkrymit.fss.file.info.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.nau.darkrymit.fss.shared.NodeDetail;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileInfoMiniResponse extends FileInfoBaseResponse implements NodeDetail {

}
