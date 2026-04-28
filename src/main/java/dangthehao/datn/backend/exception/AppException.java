package dangthehao.datn.backend.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {
  final ErrorCode errorCode;
  String message;

  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }

  public AppException(ErrorCode errorCode, Object... messages) {
    this(errorCode);
    this.message = String.format(errorCode.getMessage(), messages);
  }
}
