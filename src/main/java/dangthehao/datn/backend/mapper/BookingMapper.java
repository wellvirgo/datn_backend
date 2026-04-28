package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.booking.response.BookingHistoryItemRes;
import dangthehao.datn.backend.dto.booking.response.BookingItemRes;
import dangthehao.datn.backend.dto.booking.response.DasBookingDetailRes;
import dangthehao.datn.backend.dto.booking.response.DetailHistoryItemRes;
import dangthehao.datn.backend.entity.Booking;
import dangthehao.datn.backend.entity.BookingDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
  BookingHistoryItemRes toBookingHistoryItemRes(Booking booking);

  @Mapping(target = "roomTypeName", source = "roomType.name")
  @Mapping(target = "roomNumber", source = "room.roomNumber")
  DetailHistoryItemRes toDetailHistoryItemRes(BookingDetail detail);

  @Mapping(target = "stayDuration", expression = "java(booking.getStayDuration())")
  BookingItemRes toBookingItemRes(Booking booking);

  DasBookingDetailRes toDasBookingDetailRes(BookingHistoryItemRes bookingHistoryItemRes);
}
