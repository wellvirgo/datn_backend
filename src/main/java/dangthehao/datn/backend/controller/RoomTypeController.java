package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.common.PageableResponse;
import dangthehao.datn.backend.dto.roomType.request.*;
import dangthehao.datn.backend.dto.roomType.response.*;
import dangthehao.datn.backend.service.RoomTypeService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import dangthehao.datn.backend.util.UriUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomTypeController {
  RoomTypeService roomTypeService;

  @PostMapping("/v1/room-types/active")
  public ResponseEntity<ApiResponse<PageableResponse<RoomTypeSummaryItemRes>>> getActiveRoomTypes(
      @RequestBody SearchRoomTypeReq request) {
    PageableResponse<RoomTypeSummaryItemRes> result = roomTypeService.getRoomTypeSummary(request);
    ApiResponse<PageableResponse<RoomTypeSummaryItemRes>> apiResponse =
        ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping("/v1/room-types/availability")
  public ResponseEntity<ApiResponse<CheckAvailabilityRes>> getAvailabilityRoomTypes(
      @Valid @RequestBody CheckAvailabilityReq request) {
    CheckAvailabilityRes result = roomTypeService.getAvailableRoomTypes(request);
    ApiResponse<CheckAvailabilityRes> apiResponse = ApiResponseBuilder.success(result);
    return ResponseEntity.ok(apiResponse);
  }

  @GetMapping("/v1/room-types/{id}")
  public ResponseEntity<ApiResponse<PublicDetailRoomTypeRes>> getPublicRoomTypeDetails(
      @PathVariable Long id) {
    PublicDetailRoomTypeRes result = roomTypeService.getPublicDetailRoomTypes(id);
    ApiResponse<PublicDetailRoomTypeRes> apiResponse = ApiResponseBuilder.success(result);
    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('RECEPTIONIST')")
  @GetMapping("/v1/room-types/details/{id}")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> getRoomTypeDetails(@PathVariable Long id) {
    RoomTypeDetailRes result = roomTypeService.getRoomTypeDetails(id);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);
    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('RECEPTIONIST')")
  @PostMapping("/v1/room-types/search")
  public ResponseEntity<ApiResponse<PageableResponse<RoomTypeItemRes>>> getRoomTypes(
      @RequestBody SearchRoomTypeReq request) {
    PageableResponse<RoomTypeItemRes> result = roomTypeService.getRoomTypes(request);
    ApiResponse<PageableResponse<RoomTypeItemRes>> apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping("/v1/room-types")
  public ResponseEntity<ApiResponse<RoomTypeItemRes>> createRoomTypes(
      @Valid @RequestPart(name = "data") CreateRoomTypeReq request,
      @RequestPart(required = false, name = "thumbnail") MultipartFile thumbnail) {
    RoomTypeItemRes result = roomTypeService.createRoomTypes(request, thumbnail);
    URI location = UriUtils.generateUri(result.getId());
    ApiResponse<RoomTypeItemRes> apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.created(location).body(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PatchMapping("/v1/room-types/{id}/basic-info")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> updateBasicInfo(
      @PathVariable(name = "id") Long id, @Valid @RequestBody UpdateBasicInfoReq request) {
    RoomTypeDetailRes result = roomTypeService.updateBasicInfo(id, request);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PatchMapping("/v1/room-types/{id}/occupancy")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> updateOccupancy(
      @PathVariable(name = "id") Long id, @Valid @RequestBody UpdateOccupancyReq request) {
    RoomTypeDetailRes result = roomTypeService.updateOccupancyRoomType(id, request);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PatchMapping("/v1/room-types/{id}/room-space")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> updateRoomSpace(
      @PathVariable(name = "id") Long id, @Valid @RequestBody UpdateRoomSpaceReq request) {
    RoomTypeDetailRes result = roomTypeService.updateRoomSpace(id, request);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);
    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/v1/room-types/{id}/amenities")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> updateAmenities(
      @PathVariable(name = "id") Long id, @RequestBody UpdateRoomTypeAmenitiesReq request) {
    RoomTypeDetailRes result = roomTypeService.updateRoomTypeAmenities(id, request);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/v1/room-types/{id}/services")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> updateServices(
      @PathVariable(name = "id") Long id, @RequestBody UpdateRoomTypeServicesReq request) {
    RoomTypeDetailRes result = roomTypeService.updateRoomTypeServices(id, request);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);

    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping("v1/room-types/{id}/thumb")
  public ResponseEntity<ApiResponse<RoomTypeDetailRes>> updateRoomTypeThumb(
      @PathVariable(name = "id") Long id, @RequestParam(name = "thumb") MultipartFile file) {
    RoomTypeDetailRes result = roomTypeService.updateRoomTypeThumb(id, file);
    ApiResponse<RoomTypeDetailRes> apiResponse = ApiResponseBuilder.success(result);
    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @DeleteMapping("/v1/room-types/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteRoomType(@PathVariable(name = "id") Long id) {
    roomTypeService.deleteById(id);
    ApiResponse<Void> apiResponse = ApiResponseBuilder.success();

    return ResponseEntity.ok(apiResponse);
  }

  @PreAuthorize("hasRole('RECEPTIONIST')")
  @GetMapping("/v1/room-types/total-rooms")
  public ResponseEntity<ApiResponse<Long>> getTotalRooms() {
    return ResponseEntity.ok(ApiResponseBuilder.success(roomTypeService.getTotalRooms()));
  }
}
