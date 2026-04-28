package dangthehao.datn.backend.util;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.common.ErrorDetail;

import java.util.List;

public class ApiResponseBuilder {
  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder().code("200").message("success").data(data).build();
  }

  public static <T> ApiResponse<T> success() {
    return ApiResponse.<T>builder().code("200").message("success").build();
  }

  public static <T> ApiResponse<T> error(String code, String message, List<ErrorDetail> errors) {
    return ApiResponse.<T>builder()
        .code(code)
        .message(message)
        .errors(errors)
        .build();
  }

  public static <T> ApiResponse<T> error(String code, String message) {
    return ApiResponse.<T>builder()
        .code(code)
        .message(message)
        .build();
  }
}
