package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.common.PageableResponse;
import dangthehao.datn.backend.dto.user.request.SearchUserReq;
import dangthehao.datn.backend.dto.user.request.UserCreateReq;
import dangthehao.datn.backend.dto.user.response.CustomerItemRes;
import dangthehao.datn.backend.dto.user.response.UserBaseDto;
import dangthehao.datn.backend.dto.user.response.UserDetailRes;
import dangthehao.datn.backend.dto.user.response.UserSummaryInfo;
import dangthehao.datn.backend.service.UserService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import dangthehao.datn.backend.util.UriUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @PostMapping
  @PreAuthorize("@roleValidator.canAssignRole(authentication, #request.getRole())")
  public ResponseEntity<ApiResponse<Void>> creatUser(@Valid @RequestBody UserCreateReq request) {
    Long userId = userService.createUser(request);
    URI location = UriUtils.generateUri(userId);

    return ResponseEntity.created(location).build();
  }

  @PostMapping("/search")
  @PreAuthorize("hasRole('RECEPTIONIST')")
  public ResponseEntity<ApiResponse<PageableResponse<UserBaseDto>>> getUsers(
      @RequestBody SearchUserReq request, Authentication authentication) {
    boolean isReceptionist =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch("ROLE_RECEPTIONIST"::equals);

    PageableResponse<UserBaseDto> result = userService.getAllUsers(isReceptionist, request);
    var apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping("/customers/search")
  @PreAuthorize("hasRole('RECEPTIONIST')")
  public ResponseEntity<ApiResponse<PageableResponse<CustomerItemRes>>> getCustomers(
          @RequestBody SearchUserReq request, Authentication authentication) {
    boolean isReceptionist =
            authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("ROLE_RECEPTIONIST"::equals);

    PageableResponse<CustomerItemRes> result = userService.getAllCustomer(isReceptionist, request);
    var apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserDetailRes>> getUserDetail(
      @PathVariable Long id, Authentication authentication) {
    UserDetailRes userDetailRes = userService.getUserDetail(authentication, id);
    return ResponseEntity.ok(ApiResponseBuilder.success(userDetailRes));
  }

  @GetMapping("/summary-info")
  public ResponseEntity<ApiResponse<UserSummaryInfo>> getUserSummaryInfo(
      Authentication authentication) {
    String email = authentication.getName();
    UserSummaryInfo userSummaryInfo = userService.getUserSummaryInfo(email);
    return ResponseEntity.ok(ApiResponseBuilder.success(userSummaryInfo));
  }

  @PutMapping("/avatar/{id}")
  public ResponseEntity<ApiResponse<String>> updateUserAvatar(
      @PathVariable Long id,
      @RequestParam(name = "avatar") MultipartFile file,
      Authentication authentication) {
    String currUserEmail = authentication.getName();
    String result = userService.updateAvatar(currUserEmail, id, file);
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MANAGER')")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable(name = "id") Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(ApiResponseBuilder.success());
  }
}
