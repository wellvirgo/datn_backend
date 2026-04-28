package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.roomType.RoomTypeBookedCountPrj;
import dangthehao.datn.backend.entity.Booking;
import dangthehao.datn.backend.entity.RoomInventory;
import dangthehao.datn.backend.entity.RoomType;
import dangthehao.datn.backend.repository.BookingDetailRepository;
import dangthehao.datn.backend.repository.RoomInventoryRepository;
import dangthehao.datn.backend.repository.RoomTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoomInventoryService {
  RoomInventoryRepository roomInventoryRepo;
  RoomTypeRepository roomTypeRepo;
  BookingDetailRepository bookingDetailRepo;

  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void generateRoomInventory() {
    log.info("Begin generating room inventory");

    LocalDate today = LocalDate.now();
    LocalDate targetDate = today.plusDays(365);

    LocalDate maxDateInDb = roomInventoryRepo.findMaxDate();
    LocalDate startDate;

    if (maxDateInDb == null || maxDateInDb.isBefore(today)) startDate = today;
    else startDate = maxDateInDb.plusDays(1);

    if (startDate.isAfter(targetDate)) {
      log.info("Data in inventory is enough to {}. Skip", targetDate);
      return;
    }

    List<RoomType> activeRoomTypes = roomTypeRepo.findAllByDeletedFalseAndActiveTrue();
    if (activeRoomTypes.isEmpty()) {
      log.info("No active room types found");
      return;
    }

    List<RoomInventory> inventoriesToSave = new ArrayList<>();

    for (LocalDate date = startDate; !date.isAfter(targetDate); date = date.plusDays(1)) {
      for (RoomType roomType : activeRoomTypes) {
        RoomInventory inventory =
            RoomInventory.builder()
                .inventoryDate(date)
                .roomType(roomType)
                .totalAllotment(roomType.getTotalRooms())
                .bookedCount(0L)
                .build();
        inventoriesToSave.add(inventory);
      }
    }

    if (!inventoriesToSave.isEmpty()) {
      roomInventoryRepo.saveAll(inventoriesToSave);
      log.info(
          "Saved {} inventories from {} to {}", inventoriesToSave.size(), startDate, targetDate);
    }
  }

  @Transactional
  public void releaseRoomInventories(Booking booking) {
    List<RoomTypeBookedCountPrj> roomTypeBookedCountList =
        bookingDetailRepo.countBookedRoomByRoomType(booking.getId());

    if (roomTypeBookedCountList.isEmpty()) return;

    Map<Long, Integer> bookedCountByRoomTypeId =
        roomTypeBookedCountList.stream()
            .collect(
                Collectors.toMap(
                    RoomTypeBookedCountPrj::getRoomTypeId, RoomTypeBookedCountPrj::getBookedCount));

    List<RoomInventory> inventories =
        roomInventoryRepo.findByRoomTypeIdsInAndBookingDateBetween(
            new ArrayList<>(bookedCountByRoomTypeId.keySet()),
            booking.getCheckInDate(),
            booking.getCheckOutDate());

    for (RoomInventory inventory : inventories) {
      Integer releaseCount = bookedCountByRoomTypeId.get(inventory.getRoomType().getId());
      if (releaseCount == null) continue;

      long newCount = inventory.getBookedCount() - releaseCount;
      inventory.setBookedCount(Math.max(0, newCount));

      if (newCount < 0)
        log.warn(
            "bookedCount went below 0 for RoomInventory id={}, roomTypeId={}, bookingId={}",
            inventory.getId(),
            inventory.getRoomType().getId(),
            booking.getId());
    }

    roomInventoryRepo.saveAll(inventories);
  }
}
