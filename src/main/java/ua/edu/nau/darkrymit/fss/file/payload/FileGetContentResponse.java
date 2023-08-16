package ua.edu.nau.darkrymit.fss.file.payload;

import lombok.Value;
import org.springframework.core.io.Resource;

@Value
public class FileGetContentResponse {

  String name;

  String contentType;

  Resource resource;
}
