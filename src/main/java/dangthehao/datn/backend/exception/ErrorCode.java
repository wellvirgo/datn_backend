package dangthehao.datn.backend.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
  USER_NOT_FOUND("01", "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
  INVALID_REFRESH_TOKEN("02", "Invalid refresh token", HttpStatus.UNAUTHORIZED),
  UNAUTHENTICATED("03", "Unauthenticated", HttpStatus.UNAUTHORIZED),
  FORBIDDEN("04", "Access is denied", HttpStatus.FORBIDDEN),
  RESOURCE_NOT_FOUND("05", "%s không tìm thấy", HttpStatus.NOT_FOUND),
  DUPLICATE_RESOURCE("06", "%s đã tồn tại", HttpStatus.CONFLICT),
  BAD_REQUEST("07", "%s", HttpStatus.BAD_REQUEST),
  IO_EXCEPTION("08", "IO exception: %s", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_CREDENTIALS("09", "Bad credentials", HttpStatus.UNAUTHORIZED),
  FAILED_VALIDATION("10", "Validation failed", HttpStatus.BAD_REQUEST),
  INVENTORY_NOT_ENOUGH("11", "Inventory not enough", HttpStatus.BAD_REQUEST),
  UNSUPPORTED_HASH_ALGORITHM("12", "Not support %s", HttpStatus.INTERNAL_SERVER_ERROR),
  LIMIT_EXCEEDED("13", "Limit exceeded: %s", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR("99", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
  ;
  final String code;
  final String message;
  final HttpStatus httpStatus;

  ErrorCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
