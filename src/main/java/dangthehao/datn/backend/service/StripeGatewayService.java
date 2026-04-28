package dangthehao.datn.backend.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import dangthehao.datn.backend.constant.BookingPaymentStatus;
import dangthehao.datn.backend.constant.BookingStatus;
import dangthehao.datn.backend.constant.PaymentStatus;
import dangthehao.datn.backend.entity.Booking;
import dangthehao.datn.backend.entity.Payment;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.repository.BookingRepository;
import dangthehao.datn.backend.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class StripeGatewayService {
  PaymentRepository paymentRepository;
  BookingRepository bookingRepository;
  HotelSettingService hotelSettingService;
  RoomInventoryService roomInventoryService;

  static String NAME_PATTERN = "Thanh toán đơn đặt phòng %s";
  static String DESC_PATTERN = "Check in: %s, Check out: %s";

  @NonFinal
  @Value("${stripe.webhook.key}")
  String webhookKey;

  public Session createStripePaymentSession(Booking booking, Payment payment)
      throws StripeException {
    long deadlineMinute =
        Long.parseLong(hotelSettingService.getPaymentDeadline().getSettingValue());
    long expiresAt = Instant.now().plus(deadlineMinute, ChronoUnit.MINUTES).getEpochSecond();

    SessionCreateParams params =
        SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setExpiresAt(expiresAt)
            .setSuccessUrl("http://localhost:4200/payment-result?status=success")
            .setCancelUrl("http://localhost:4200/payment-result?status=error")
            .putAllMetadata(
                Map.of(
                    "booking_id", booking.getId().toString(),
                    "payment_id", payment.getId().toString()))
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(createPriceData(booking))
                    .build())
            .build();

    return Session.create(params);
  }

  @Transactional
  public void handleStripeWebhook(String payload, String sigHeader) {
    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, webhookKey);
    } catch (SignatureVerificationException e) {
      log.error("Invalid signature", e);
      throw new AppException(ErrorCode.BAD_REQUEST, "Invalid signature");
    } catch (Exception e) {
      log.error("Error in webhook", e);
      throw new AppException(ErrorCode.BAD_REQUEST, "Error in webhook");
    }

    switch (event.getType()) {
      case "checkout.session.completed":
        processSuccessEvent(event);
        break;
      case "checkout.session.expired":
        processFailureEvent(event);
        break;
      default:
        log.error("Unknown event type: {}", event.getType());
    }
  }

  private void processSuccessEvent(Event event) {
    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
    if (dataObjectDeserializer.getObject().isPresent()) {
      StripeObject stripeObject = dataObjectDeserializer.getObject().get();
      Session session = (Session) stripeObject;
      String paymentIdString = session.getMetadata().get("payment_id");
      BigDecimal amount = BigDecimal.valueOf(session.getAmountTotal());

      Payment payment =
          paymentRepository
              .findById(Long.parseLong(paymentIdString))
              .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, paymentIdString));

      if (PaymentStatus.SUCCESS.name().equals(payment.getStatus())) {
        return;
      }
      payment.setStatus(PaymentStatus.SUCCESS.name());
      payment.setPaidAt(LocalDateTime.now());
      paymentRepository.save(payment);

      Booking booking = payment.getBooking();
      booking.setBalanceDue(booking.getBalanceDue().subtract(amount));
      booking.setPaymentStatus(BookingPaymentStatus.PAID.name());
      booking.setBookingStatus(BookingStatus.CONFIRMED.name());
      bookingRepository.save(booking);
    }
  }

  private void processFailureEvent(Event event) {
    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
    if (dataObjectDeserializer.getObject().isPresent()) {
      StripeObject stripeObject = dataObjectDeserializer.getObject().get();
      Session session = (Session) stripeObject;
      String paymentIdString = session.getMetadata().get("payment_id");

      Payment payment =
          paymentRepository
              .findById(Long.parseLong(paymentIdString))
              .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, paymentIdString));

      if (PaymentStatus.FAILED.name().equals(payment.getStatus())) {
        return;
      }
      payment.setStatus(PaymentStatus.FAILED.name());
      payment.setPaidAt(LocalDateTime.now());
      paymentRepository.save(payment);

      Booking booking = payment.getBooking();
      booking.setPaymentStatus(BookingPaymentStatus.UNPAID.name());
      booking.setBookingStatus(BookingStatus.EXPIRED.name());
      bookingRepository.save(booking);

      roomInventoryService.releaseRoomInventories(booking);
    }
  }

  private SessionCreateParams.LineItem.PriceData createPriceData(Booking booking) {
    return SessionCreateParams.LineItem.PriceData.builder()
        .setCurrency("vnd")
        .setUnitAmount(booking.getTotalAmount().longValue())
        .setProductData(createProductData(booking))
        .build();
  }

  private SessionCreateParams.LineItem.PriceData.ProductData createProductData(Booking booking) {
    return SessionCreateParams.LineItem.PriceData.ProductData.builder()
        .setName(String.format(NAME_PATTERN, booking.getBookingCode()))
        .setDescription(
            String.format(
                DESC_PATTERN,
                booking.getCheckInDate().toString(),
                booking.getCheckOutDate().toString()))
        .build();
  }
}
