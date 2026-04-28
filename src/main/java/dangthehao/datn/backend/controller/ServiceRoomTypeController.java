package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.serviceRoomType.ServiceItemRes;
import dangthehao.datn.backend.service.ServiceRoomTypeService;
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
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceRoomTypeController {
  ServiceRoomTypeService serviceRoomTypeService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ServiceItemRes>>> getAll() {
    List<ServiceItemRes> result = serviceRoomTypeService.getAll();
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }
}
