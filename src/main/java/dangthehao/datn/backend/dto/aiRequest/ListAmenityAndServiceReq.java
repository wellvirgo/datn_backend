package dangthehao.datn.backend.dto.aiRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;

@Getter
public class ListAmenityAndServiceReq {
  @JsonProperty(required = false)
  @JsonPropertyDescription(
      "Danh sách các từ khóa về dịch vụ hoặc tiện ích mà khách hàng muốn tìm kiếm hoặc kiểm tra (Ví dụ: ['hồ bơi', 'spa', 'gym', 'bãi đỗ xe']). "
          + "HƯỚNG DẪN QUAN TRỌNG DÀNH CHO AI: "
          + "1. TRƯỜNG HỢP LIỆT KÊ: Nếu khách hàng hỏi chung chung như 'Khách sạn có những tiện ích gì?',"
          + " 'Kể tên các dịch vụ', hãy truyền vào một mảng RỖNG [] (hoặc null) để lấy toàn bộ danh sách. "
          + "2. TRƯỜNG HỢP KIỂM TRA: Nếu khách hàng hỏi cụ thể xem khách sạn có một"
          + " hoặc nhiều tiện ích nhất định không (VD: 'Khách sạn có hồ bơi và ăn sáng không?'),"
          + " hãy trích xuất các từ khóa đó ('hồ bơi', 'ăn sáng') và đưa vào mảng này.")
  String[] keywords;
}
