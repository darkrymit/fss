package ua.edu.nau.darkrymit.fss.file.version.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.nau.darkrymit.fss.user.payload.UserMiniResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileVersionStandardResponse extends FileVersionBaseResponse {

  private Long createdAt;

  private UserMiniResponse createdBy;

}
