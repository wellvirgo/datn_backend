package dangthehao.datn.backend.dto.aiRequest;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription(
"""
Sử dụng hàm này khi khách hàng hỏi về BẤT KỲ thông tin cài đặt,
quy định, hay chính sách nào của khách sạn (ví dụ: giờ nhận/trả phòng,
chính sách hủy, thú cưng, wifi, v.v.). Hàm này sẽ trả về toàn bộ danh sách
quy định. Bạn hãy tự đối chiếu câu hỏi của khách với danh sách trả về để đưa ra câu trả lời.
""")
public class HotelSettingDetailReq {}
