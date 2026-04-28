package dangthehao.datn.backend.config;

import dangthehao.datn.backend.dto.aiRequest.HotelSettingDetailReq;
import dangthehao.datn.backend.dto.aiRequest.ListRoomTypeReq;
import dangthehao.datn.backend.dto.hotelSetting.HotelSettingItemPrj;
import dangthehao.datn.backend.dto.aiRequest.RoomTypeDetailReq;
import dangthehao.datn.backend.dto.roomType.request.CheckAvailabilityReq;
import dangthehao.datn.backend.dto.roomType.response.CheckAvailabilityRes;
import dangthehao.datn.backend.dto.roomType.response.PublicDetailRoomTypeRes;
import dangthehao.datn.backend.service.HotelSettingService;
import dangthehao.datn.backend.service.RoomTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class AIToolConfig {
  RoomTypeService roomTypeService;
  HotelSettingService hotelSettingService;

  @Bean
  @Description(
      "Hàm này dùng để tra cứu THÔNG TIN CHI TIẾT của một loại phòng cụ thể trong khách sạn. "
          + "Gọi hàm này KHI khách hàng hỏi các câu như: 'Phòng [Tên Phòng] có to không?', 'Phòng [Tên Phòng] có những tiện ích gì?', 'Giới thiệu cho tôi phòng [Tên Phòng]'. "
          + "Hàm sẽ trả về diện tích, loại giường, sức chứa tối đa, hướng nhìn và danh sách các tiện ích. "
          + "LƯU Ý: Tuyệt đối KHÔNG dùng hàm này để kiểm tra xem ngày đó còn phòng trống hay không.")
  public Function<RoomTypeDetailReq, PublicDetailRoomTypeRes> searchRoomTypeDetails() {
    return request -> roomTypeService.searchRoomTypeDetail(request.getKeyword());
  }

  @Bean
  @Description(
      "Lấy toàn bộ thông tin cài đặt và quy định của khách sạn để trả lời thắc mắc của khách.")
  public Function<HotelSettingDetailReq, List<HotelSettingItemPrj>> getHotelSettingDetail() {
    return request -> {
      List<HotelSettingItemPrj> result = hotelSettingService.getAllSetting();
      result.forEach(item -> log.info(" - {}: {}", item.settingKey(), item.settingValue()));
      return result;
    };
  }

  @Bean
  @Description(
"""
Lấy danh sách tên tất cả các loại phòng (hạng phòng) hiện có trong khách sạn.\s
Sử dụng hàm này khi khách hàng hỏi về các loại phòng khách sạn đang cung cấp,\s
hoặc muốn biết khách sạn có tổng cộng bao nhiêu loại phòng khác nhau (bằng cách đếm số lượng kết quả trả về).
""")
  public Function<ListRoomTypeReq, List<String>> getAllRoomTypeNames() {
    return request -> roomTypeService.getAllRoomTypeNames();
  }

  @Bean
  @Description(
"""
Tìm kiếm và kiểm tra danh sách các loại phòng còn trống dựa trên ngày nhận phòng (check-in) và ngày trả phòng (check-out).\s
Kết quả cung cấp toàn bộ chi tiết về phòng (giá cả, sức chứa tối đa, loại giường, diện tích, tiện nghi, số lượng phòng trống hiện tại),\s
tiêu chí tìm kiếm và chính sách hủy phòng (cancellation policy).\s
LƯU Ý QUAN TRỌNG DÀNH CHO AI:\s
1. LƯU GIỮ NGỮ CẢNH: Khi đã gọi hàm này và có kết quả,\s
HÃY SỬ DỤNG LẠI dữ liệu này để trả lời các câu hỏi nối tiếp của khách hàng về:\s
phòng trống, giá cả, tiện nghi, 'phòng này chứa được bao nhiêu người?', 'chính sách hủy ra sao?', 'có cho kê thêm giường không?',... và các câu hỏi liên quan\s
thay vì gọi lại hàm này nhiều lần với cùng một khoảng thời gian check-in/check-out.\s
TUYỆT ĐỐI KHÔNG GỌI LẠI HÀM NÀY trừ khi khách hàng muốn thay đổi ngày nhận/trả phòng khác.\s
2. TIẾT KIỆM TOKEN: Bỏ qua hoàn toàn trường dữ liệu 'thumbnail'.\s
Không được phép sinh ra hoặc trả về bất kỳ đường dẫn hình ảnh nào cho khách.
""")
  public Function<CheckAvailabilityReq, CheckAvailabilityRes> checkAvailabilityRooms() {
    return roomTypeService::getAvailableRoomTypes;
  }
}
