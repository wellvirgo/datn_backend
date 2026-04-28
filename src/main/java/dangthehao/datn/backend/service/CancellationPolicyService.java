package dangthehao.datn.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dangthehao.datn.backend.dto.hotelSetting.CancellationPolicyRes;
import dangthehao.datn.backend.entity.HotelSetting;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CancellationPolicyService {
  ObjectMapper objectMapper;
  HotelSettingService hotelSettingService;

  public CancellationPolicyRes buildCancellationPolicy(LocalDate checkIn) {
    LocalDate currentDate = LocalDate.now();
    HotelSetting cancellationPolicy = hotelSettingService.getCancellationPolicy();

    CancellationPolicyRes res = new CancellationPolicyRes();

    try {
      List<Map<String, Long>> rawPolicy =
          objectMapper.readValue(
              cancellationPolicy.getSettingValue(),
              new TypeReference<List<Map<String, Long>>>() {});
      res.setRawPolicy(rawPolicy);

      rawPolicy.sort((a, b) -> b.get("days_before").compareTo(a.get("days_before")));
      Map<String, Long> activeRule = null;
      LocalDate deadlineOfActiveRule = null;

      for (Map<String, Long> rule : rawPolicy) {
        Long daysBefore = rule.get("days_before");
        LocalDate ruleDeadline = checkIn.minusDays(daysBefore);

        if (!ruleDeadline.isBefore(currentDate)) {
          activeRule = rule;
          deadlineOfActiveRule = ruleDeadline;
          break;
        }
      }

      if (activeRule != null) {
        Long refundPercent = activeRule.get("refund_percent");
        String formattedDate =
            deadlineOfActiveRule.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if (refundPercent == 100L) {
          res.setStatus("FREE_CANCELLATION");
          res.setDisplayText("Miễn phí hủy phòng trước ngày " + formattedDate);
        } else if (refundPercent > 0L) {
          res.setStatus("PARTIAL_REFUND");
          res.setDisplayText(
              String.format("Hủy phòng hoàn %d%% trước ngày %s", refundPercent, formattedDate));
        } else {
          res.setStatus("NON_REFUNDABLE");
          res.setDisplayText("Không hoàn tiền khi hủy phòng");
        }
      } else {
        res.setStatus("NON_REFUNDABLE");
        res.setDisplayText("Không hoàn tiền khi hủy phòng");
      }

    } catch (Exception e) {
      log.error(e.getMessage());
      res.setStatus("ERROR");
      res.setDisplayText("Đang cập nhật chính sách");
      res.setRawPolicy(null);
    }

    return res;
  }
}
