package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.booking.request.BookingReq;
import dangthehao.datn.backend.dto.booking.request.DasSearchBookingReq;
import dangthehao.datn.backend.dto.booking.response.BookingHistoryItemRes;
import dangthehao.datn.backend.dto.booking.response.BookingItemRes;
import dangthehao.datn.backend.dto.booking.response.CreatedBookingRes;
import dangthehao.datn.backend.dto.booking.response.DasBookingDetailRes;
import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.common.PageableRequest;
import dangthehao.datn.backend.dto.common.PageableResponse;
import dangthehao.datn.backend.entity.User;
import dangthehao.datn.backend.service.BookingService;
import dangthehao.datn.backend.service.UserService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
  BookingService bookingService;
  UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<CreatedBookingRes>> createBooking(
      @Valid @RequestBody BookingReq bookingReq, @AuthenticationPrincipal Jwt jwt) {
    User currentUser = userService.getUserByEmail(jwt.getSubject());
    CreatedBookingRes result = bookingService.createBooking(bookingReq, currentUser);

    ApiResponse<CreatedBookingRes> apiResponse = ApiResponseBuilder.success(result);
    return ResponseEntity.ok(apiResponse);
  }

  @PostMapping("/history")
  public ResponseEntity<ApiResponse<PageableResponse<BookingHistoryItemRes>>> getBookingHistory(
      @RequestBody PageableRequest request, @AuthenticationPrincipal Jwt jwt) {
    String userEmail = jwt.getSubject();
    PageableResponse<BookingHistoryItemRes> result =
        bookingService.getBookingHistory(request, userEmail);

    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }

  @PreAuthorize("hasRole('RECEPTIONIST')")
  @PostMapping("/management/search")
  public ResponseEntity<ApiResponse<PageableResponse<BookingItemRes>>> searchBookings(
      @RequestBody DasSearchBookingReq request) {
    PageableResponse<BookingItemRes> result = bookingService.getBookingItems(request);
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }

  @PreAuthorize("hasRole('RECEPTIONIST')")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<DasBookingDetailRes>> getBookingDetails(@PathVariable Long id) {
    DasBookingDetailRes result = bookingService.getBookingDetail(id);
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }
}
