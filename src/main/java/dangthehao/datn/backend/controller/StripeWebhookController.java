package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.service.StripeGatewayService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StripeWebhookController {
  StripeGatewayService stripeGatewayService;

  @PostMapping("/ipn")
  public ResponseEntity<ApiResponse<Void>> handleStripeWebhook(
      @RequestBody String payload, @RequestHeader("Stripe-Signature") String signHeader) {
    stripeGatewayService.handleStripeWebhook(payload, signHeader);
    return ResponseEntity.ok(ApiResponseBuilder.success());
  }
}
