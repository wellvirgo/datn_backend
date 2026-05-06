package dangthehao.datn.backend.config;

import dangthehao.datn.backend.dto.aiRequest.HotelSettingDetailReq;
import dangthehao.datn.backend.dto.aiRequest.ListAmenityAndServiceReq;
import dangthehao.datn.backend.dto.aiRequest.ListRoomTypeReq;
import dangthehao.datn.backend.dto.hotelSetting.HotelSettingItemPrj;
import dangthehao.datn.backend.dto.aiRequest.RoomTypeDetailReq;
import dangthehao.datn.backend.dto.roomType.request.CheckAvailabilityReq;
import dangthehao.datn.backend.dto.roomType.response.CheckAvailabilityRes;
import dangthehao.datn.backend.dto.roomType.response.PublicDetailRoomTypeRes;
import dangthehao.datn.backend.dto.serviceRoomType.ServiceItemRes;
import dangthehao.datn.backend.service.AmenityService;
import dangthehao.datn.backend.service.HotelSettingService;
import dangthehao.datn.backend.service.RoomTypeService;
import dangthehao.datn.backend.service.ServiceRoomTypeService;
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
  AmenityService amenityService;
  ServiceRoomTypeService serviceRoomTypeService;

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
          SỬ DỤNG HÀM NÀY cho MỌI yêu cầu tìm phòng, kiểm tra giá, hoặc tư vấn phòng theo số lượng người, khoảng giá, và tiện nghi.\s
          Kết quả cung cấp toàn bộ chi tiết về phòng (giá cả, sức chứa tối đa, loại giường, diện tích, tiện nghi, số lượng phòng trống hiện tại),\s
          tiêu chí tìm kiếm và chính sách hủy phòng (cancellation policy).\s
          QUY TRÌNH XỬ LÝ CỦA AI:
          1. Chuẩn bị tham số: Lấy ngày check-in và check-out. Nếu khách chỉ cung cấp ngày check-in mà không nói ngày trả phòng,\s
          MẶC ĐỊNH check-out là ngày hôm sau.
          2. Gọi hàm: Truyền ngày check-in và check-out vào hàm để lấy danh sách tất cả phòng trống.
          3. Lọc và Đề xuất: Sau khi có kết quả, AI TỰ ĐỘNG LỌC dữ liệu trả về\s
          (VD: phòng có sức chứa >= số người khách yêu cầu, giá nằm trong khoảng X đến Y) để chọn ra các phòng phù hợp nhất và tư vấn.
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

  @Bean
  @Description(
          """
          Lấy danh sách toàn bộ các tiện nghi (amenities) và dịch vụ (services) mà khách sạn đang cung cấp.\s
          Sử dụng hàm này trong 2 trường hợp:\s
          1. YÊU CẦU LIỆT KÊ: Khi khách hàng muốn biết khách sạn có những tiện nghi, dịch vụ gì.\s
          2. YÊU CẦU KIỂM TRA: Khi khách hàng hỏi cụ thể xem khách sạn có một tiện ích/dịch vụ nào đó không\s
          (Ví dụ: 'Khách sạn có hồ bơi không?', 'Có dịch vụ giặt ủi không?').\s
          LƯU Ý DÀNH CHO AI:\s
          Hàm này luôn trả về một chuỗi chứa TOÀN BỘ danh sách.\s
          AI phải tự đọc, quét thông tin trong chuỗi kết quả này để đối chiếu với câu hỏi của khách.\s
          Nếu khách hỏi về một tiện ích cụ thể, hãy kiểm tra xem nó có nằm trong chuỗi trả về không\s
          và trả lời khách một cách tự nhiên. Chú ý thông tin trong ngoặc đơn sau tên tiện nghi (nếu có)\s
          để biết tiện nghi đó có cần khách yêu cầu trước (on request) hay không để tư vấn thêm.
          """)
  public Function<ListAmenityAndServiceReq, String> listAmenityAndService() {
    return request -> {
      List<String> amenities =
              amenityService.getAll().stream()
                      .map(res -> String.format("%s (%s)", res.getName(), res.getOnRequest()))
                      .toList();
      List<String> services =
              serviceRoomTypeService.getAll().stream().map(ServiceItemRes::getName).toList();

      return String.format(
              "Danh sách tiện nghi của khách sạn: %s. Danh sách dịch vụ cung cấp: %s.",
              String.join(", ", amenities), String.join(", ", services));
    };
  }
}
