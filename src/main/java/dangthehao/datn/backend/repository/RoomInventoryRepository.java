package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.RoomInventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {
  @Query("select max(ri.inventoryDate) from RoomInventory ri")
  LocalDate findMaxDate();

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select ri from RoomInventory ri where"
          + " ri.roomType.id = :roomTypeId"
          + " and ri.inventoryDate >= :startDate"
          + " and ri.inventoryDate < :endDate"
          + " order by ri.inventoryDate")
  List<RoomInventory> findAndLockByDates(Long roomTypeId, LocalDate startDate, LocalDate endDate);

  @Query("select ri from RoomInventory ri where ri.roomType.id in :roomTypeIds" +
          " and ri.inventoryDate >= :startDate" +
          " and ri.inventoryDate < :endDate")
  List<RoomInventory> findByRoomTypeIdsInAndBookingDateBetween(
      List<Long> roomTypeIds, LocalDate startDate, LocalDate endDate);
}
