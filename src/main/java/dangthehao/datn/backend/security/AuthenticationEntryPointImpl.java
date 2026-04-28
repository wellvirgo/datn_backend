package dangthehao.datn.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
  ObjectMapper mapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
    ApiResponse<Void> apiResponse =
        ApiResponseBuilder.error(errorCode.getCode(), errorCode.getMessage());
    mapper.writeValue(response.getOutputStream(), apiResponse);
  }
}
