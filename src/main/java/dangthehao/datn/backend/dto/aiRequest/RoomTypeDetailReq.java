package dangthehao.datn.backend.dto.aiRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomTypeDetailReq {
  @JsonProperty(required = true)
  @JsonPropertyDescription(
      """
       Từ khóa tên loại phòng mà khách hàng muốn tìm hiểu (ví dụ: 'deluxe', 'đê lúc', 'phòng gia đình', 'suite').
       Hãy trích xuất chuỗi khách hàng nhập vào nguyên bản nhất có thể.
       KHÔNG CẦN cố gắng sửa lỗi chính tả vì hệ thống backend đã tự động xử lý tìm kiếm gần đúng (Fuzzy Match).
       """)
  String keyword;
}
