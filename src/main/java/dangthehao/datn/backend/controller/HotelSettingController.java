package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.service.HotelSettingService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/hotel-settings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelSettingController {
  HotelSettingService hotelSettingService;

  @GetMapping("/check-in-out-policy")
  public ResponseEntity<ApiResponse<Map<String, String>>> getCheckInOutPolicy() {
    Map<String, String> result = hotelSettingService.getCheckInOutPolicy();
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }
}
