package dangthehao.datn.backend.dto.roomType.request;

import dangthehao.datn.backend.constant.SmokingPolicy;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record CreateRoomTypeReq(
    @NotBlank(message = "Tên loại phòng không được để trống")
        @Size(min = 3, max = 100, message = "Tên loại phòng phải từ 3 đến 100 ký tự")
        String name,
    @NotNull(message = "Cần chọn kiểu giường") Long bedTypeId,
    @NotNull(message = "Số người lớn tiêu chuẩn không được để trống")
        @Min(value = 1, message = "Phòng phải có sức chứa ít nhất 1 người lớn")
        @Max(value = 15, message = "Số người lớn tiêu chuẩn vượt quá giới hạn thực tế")
        Short baseAdults,
    @NotNull(message = "Số trẻ em tiêu chuẩn không được để trống")
        @Min(value = 0, message = "Số trẻ em tiêu chuẩn không được là số âm")
        @Max(value = 15, message = "Số trẻ em tiêu chuẩn vượt quá giới hạn thực tế")
        Short baseChildren,
    @NotNull(message = "Số người lớn tối đa không được để trống")
        @Min(value = 1, message = "Số người lớn tối đa phải từ 1 trở lên")
        @Max(value = 20, message = "Số người lớn tối đa vượt quá giới hạn thực tế")
        Short maxAdults,
    @NotNull(message = "Số trẻ em tối đa không được để trống")
        @Min(value = 0, message = "Số trẻ em tối đa không được là số âm")
        @Max(value = 20, message = "Số trẻ em tối đa vượt quá giới hạn thực tế")
        Short maxChildren,
    @NotNull(message = "Diện tích phòng không được để trống")
        @DecimalMin(value = "5.0", message = "Diện tích phòng tối thiểu là 5 m2")
        @Digits(
            integer = 5,
            fraction = 2,
            message =
                "Diện tích phòng không hợp lệ (tối đa 5 chữ số phần nguyên và 2 chữ số thập phân)")
        BigDecimal areaSize,
    @NotBlank(message = "Cách bố trí giường không được để trống")
        @Size(max = 255, message = "Mô tả bố trí giường không vượt quá 255 ký tự")
        String bedArrangement,
    @Size(max = 100, message = "Loại hướng nhìn không vượt quá 100 ký tự") String viewType,
    @NotNull(message = "Cần xác định phòng có cho phép thêm giường phụ không")
        Boolean extraBedAllowed,
    @NotNull(message = "Giá cơ bản không được để trống")
        @DecimalMin(value = "0.0", message = "Giá cơ bản không được là số âm")
        @Digits(integer = 12, fraction = 2, message = "Giá cơ bản quá lớn hoặc sai định dạng")
        BigDecimal basePrice,
    @DecimalMin(value = "0.0", message = "Giá giường phụ không được là số âm")
        @Digits(integer = 12, fraction = 2, message = "Giá giường phụ quá lớn hoặc sai định dạng")
        BigDecimal extraBedPrice,
    @Size(max = 2000, message = "Mô tả phòng tối đa 2000 ký tự") String description,
    @NotNull(message = "Cần thiết lập chính sách hút thuốc") SmokingPolicy smokingPolicy,
    @NotBlank(message = "Loại phòng tắm không được để trống") String bathroomType,
    @NotNull(message = "Tổng số phòng không được để trống") @Min(value = 1) Long totalRooms)
    implements Serializable {}
