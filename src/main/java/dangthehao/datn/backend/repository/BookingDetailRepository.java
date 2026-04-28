package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.roomType.RoomTypeBookedCountPrj;
import dangthehao.datn.backend.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    @Query("select bkd.roomType.id as roomTypeId, count(bkd.roomType.id) as bookedCount from BookingDetail bkd" +
            " where bkd.booking.id = :bookingId" +
            " group by bkd.roomType.id")
    List<RoomTypeBookedCountPrj> countBookedRoomByRoomType(Long bookingId);
}
