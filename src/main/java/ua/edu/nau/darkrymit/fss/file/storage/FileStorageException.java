package ua.edu.nau.darkrymit.fss.file.storage;

import java.io.Serial;

public class FileStorageException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -7359702643622106790L;

  public FileStorageException() {
    super();
  }

  public FileStorageException(String s, Throwable cause) {
    super(s, cause);
  }

  public FileStorageException(Throwable cause) {
    super(cause);
  }

  public FileStorageException(String s) {
    super(s);
  }
}
