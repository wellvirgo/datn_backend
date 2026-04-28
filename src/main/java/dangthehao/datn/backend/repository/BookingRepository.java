package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BookingRepository
    extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
  @EntityGraph(
      attributePaths = {"bookingDetails", "bookingDetails.room", "bookingDetails.roomType"})
  Page<Booking> findByUserEmail(String email, Pageable pageable);

  @EntityGraph(
      attributePaths = {"bookingDetails", "bookingDetails.room", "bookingDetails.roomType", "user"})
  @Query("select bk from Booking bk where bk.id = :id")
  Optional<Booking> findByIdFetchAll(Long id);

  @NonNull
  Page<Booking> findAll(Specification<Booking> spec, @NonNull Pageable pageable);

  Long countByUserId(Long userId);

  @Query("select sum(bk.totalAmount) from Booking bk where bk.user.id = :userId " +
          "and bk.bookingStatus = 'CHECK_OUT' and bk.paymentStatus = 'PAID'")
  BigDecimal calculateTotalSpentByUserId(Long userId);
}
