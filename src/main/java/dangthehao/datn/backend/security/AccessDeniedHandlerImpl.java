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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
  ObjectMapper mapper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ApiResponse<Void> apiResponse =
        ApiResponseBuilder.error(ErrorCode.FORBIDDEN.getCode(), ErrorCode.FORBIDDEN.getMessage());
    mapper.writeValue(response.getOutputStream(), apiResponse);
  }
}
