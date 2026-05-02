package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.hotelSetting.HotelSettingItemPrj;
import dangthehao.datn.backend.entity.HotelSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelSettingRepository extends JpaRepository<HotelSetting, String> {
  @Query("select h from HotelSetting h where h.settingKey = 'GLOBAL_CANCELLATION_POLICY'")
  Optional<HotelSetting> findCancellationPolicy();

  @Query("select h from HotelSetting h where h.settingKey = 'PAYMENT_DEADLINE_MINUTES'")
  Optional<HotelSetting> findPaymentDeadline();

  @Query(
      "select new dangthehao.datn.backend.dto.hotelSetting.HotelSettingItemPrj(settingKey, settingValue, description) from HotelSetting where isPublic = true")
  List<HotelSettingItemPrj> findAllSetting();

  @Query(
      "select h from HotelSetting h where h.settingKey in ('CHECK_IN_TIME', 'CHECK_OUT_TIME') and h.isPublic = true")
  List<HotelSetting> findCheckInOutPolicy();
}
