package dangthehao.datn.backend.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import dangthehao.datn.backend.entity.Booking;
import dangthehao.datn.backend.entity.Payment;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentService {
  PaymentRepository paymentRepo;
  StripeGatewayService stripeGatewayService;

  @Transactional
  public String createPayment(Booking booking) {
    Payment payment =
        Payment.builder()
            .booking(booking)
            .paymentMethod("STRIPE")
            .amount(booking.getTotalAmount())
            .build();
    payment = paymentRepo.save(payment);

    String paymentUrl;
    try {
      Session session = stripeGatewayService.createStripePaymentSession(booking, payment);
      payment.setTransactionNo(session.getId());
      paymentRepo.save(payment);

      paymentUrl = session.getUrl();
    } catch (StripeException e) {
      log.error("Failed to create Stripe payment session", e);
      throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    return paymentUrl;
  }
}
