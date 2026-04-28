package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.amenity.AmenityItemRes;
import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.service.AmenityService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/amenities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AmenityController {
  AmenityService amenityService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<AmenityItemRes>>> getAll() {
    List<AmenityItemRes> result = amenityService.getAll();
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }
}
