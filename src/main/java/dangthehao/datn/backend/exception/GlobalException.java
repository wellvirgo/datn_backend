package dangthehao.datn.backend.exception;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.common.ErrorDetail;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalException {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> exception(Exception exception) {
    log.error("Uncaught Exception: {}", exception.getMessage(), exception);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponseBuilder.error(errorCode.getCode(), errorCode.getMessage()));
  }

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<Void>> appException(AppException e) {
    ErrorCode errorCode = e.getErrorCode();
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(ApiResponseBuilder.error(errorCode.getCode(), e.getMessage()));
  }

  @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
  public ResponseEntity<ApiResponse<Void>> badCredentialsException(RuntimeException e) {
    log.error("Bad Credentials Exception: {}", e.getMessage(), e);
    ErrorCode errorCode = ErrorCode.BAD_CREDENTIALS;
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(ApiResponseBuilder.error(errorCode.getCode(), e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handle(MethodArgumentNotValidException ex) {
    ErrorCode errorCode = ErrorCode.FAILED_VALIDATION;

    List<ErrorDetail> errorDetails = extractMethodArgumentErrors(ex);
    ApiResponse<Void> errorResponse =
        ApiResponseBuilder.error(errorCode.getCode(), errorCode.getMessage(), errorDetails);

    return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
  }

  @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
  public ResponseEntity<ApiResponse<Void>> handle(Exception e) {
    log.error(e.getMessage(), e);

    ErrorCode errorCode = ErrorCode.FORBIDDEN;
    String message = errorCode.getMessage() + ": " + "not competent enough";

    ApiResponse<Void> apiResponse = ApiResponseBuilder.error(errorCode.getCode(), message);
    return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
  }

  private List<ErrorDetail> extractMethodArgumentErrors(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getAllErrors().stream()
        .map(
            error -> {
              if (error instanceof FieldError fieldError) {
                return ErrorDetail.builder()
                    .object(fieldError.getObjectName())
                    .field(fieldError.getField())
                    .error(fieldError.getDefaultMessage())
                    .build();
              }

              return ErrorDetail.builder()
                  .object(error.getObjectName())
                  .error(error.getDefaultMessage())
                  .build();
            })
        .toList();
  }
}
