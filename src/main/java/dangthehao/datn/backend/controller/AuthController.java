package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.common.AuthRequest;
import dangthehao.datn.backend.dto.user.request.UserRegistrationReq;
import dangthehao.datn.backend.security.service.AuthService;
import dangthehao.datn.backend.service.UserService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import dangthehao.datn.backend.util.UriUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;
  UserService userService;

  @NonFinal
  @Value("${security.jwt.refresh-token-alive-time}")
  long refreshTokenAliveTime;

  @PostMapping("/v1/auth")
  public ResponseEntity<ApiResponse<String>> authenticate(@RequestBody AuthRequest authRequest) {
    Map<String, String> tokenPair = authService.authenticate(authRequest);
    return getAuthResponse(tokenPair);
  }

  @PostMapping("v1/auth/register")
  public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserRegistrationReq request) {
    Long userId = userService.register(request);
    URI location = UriUtils.generateUri(userId);

    return ResponseEntity.created(location).body(ApiResponseBuilder.success());
  }

  @PostMapping("/v1/auth/refresh")
  public ResponseEntity<ApiResponse<String>> refresh(
      @CookieValue(name = "refresh_token", required = false) String refreshToken) {
    Map<String, String> tokenPair = authService.renewTokenPair(refreshToken);
    return getAuthResponse(tokenPair);
  }

  @PostMapping("/v1/auth/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @CookieValue(name = "refresh_token", required = false) String refreshToken) {
    authService.logout(refreshToken);
    ResponseCookie cookie =
        buildResponseCookie("refresh_token", null, "/api/v1/auth", refreshTokenAliveTime);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponseBuilder.success());
  }

  private ResponseCookie buildResponseCookie(String key, String value, String path, long maxAge) {
    return ResponseCookie.from(key, value)
        .maxAge(maxAge)
        .httpOnly(true)
        .secure(false)
        .path(path)
        .sameSite("Lax")
        .build();
  }

  private ResponseEntity<ApiResponse<String>> getAuthResponse(Map<String, String> tokenPair) {
    ResponseCookie cookie =
        buildResponseCookie(
            "refresh_token", tokenPair.get("refresh_token"), "/api/v1/auth", refreshTokenAliveTime);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponseBuilder.success(tokenPair.get("access_token")));
  }
}
