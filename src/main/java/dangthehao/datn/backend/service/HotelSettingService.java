package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.hotelSetting.HotelSettingItemPrj;
import dangthehao.datn.backend.entity.HotelSetting;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.repository.HotelSettingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class HotelSettingService {
  HotelSettingRepository hotelSettingRepo;

  public HotelSetting getCancellationPolicy() {
    return hotelSettingRepo
        .findCancellationPolicy()
        .orElseThrow(
            () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "No cancellation policy found"));
  }

  public HotelSetting getPaymentDeadline() {
    return hotelSettingRepo
        .findPaymentDeadline()
        .orElseThrow(
            () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "No payment deadline found"));
  }

  public List<HotelSettingItemPrj> getAllSetting() {
    return hotelSettingRepo.findAllSetting();
  }
}
