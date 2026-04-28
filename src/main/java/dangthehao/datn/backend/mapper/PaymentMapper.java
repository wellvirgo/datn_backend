package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.payment.response.PaymentHistoryRes;
import dangthehao.datn.backend.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
  PaymentHistoryRes toPaymentHistoryRes(Payment payment);
}
