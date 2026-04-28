package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.bedType.response.BedTypeBaseDto;
import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.service.BedTypeService;
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
@RequestMapping("/api/v1/bed-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BedTypeController {
  BedTypeService bedTypeService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<BedTypeBaseDto>>> getAllBedTypes() {
    List<BedTypeBaseDto> bedTypes = bedTypeService.getAllBedTypes();
    return ResponseEntity.ok(ApiResponseBuilder.success(bedTypes));
  }
}
