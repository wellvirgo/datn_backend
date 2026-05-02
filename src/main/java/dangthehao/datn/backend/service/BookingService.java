package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.booking.request.BookingReq;
import dangthehao.datn.backend.dto.booking.request.DasSearchBookingReq;
import dangthehao.datn.backend.dto.booking.request.RoomBookingReq;
import dangthehao.datn.backend.dto.booking.response.*;
import dangthehao.datn.backend.dto.common.PageableRequest;
import dangthehao.datn.backend.dto.common.PageableResponse;
import dangthehao.datn.backend.dto.payment.response.PaymentHistoryRes;
import dangthehao.datn.backend.entity.*;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.mapper.BookingMapper;
import dangthehao.datn.backend.mapper.PaymentMapper;
import dangthehao.datn.backend.repository.BookingDetailRepository;
import dangthehao.datn.backend.repository.BookingRepository;
import dangthehao.datn.backend.repository.RoomInventoryRepository;
import dangthehao.datn.backend.util.BookingCodeGenerator;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BookingService {
  BookingRepository bookingRepo;
  BookingDetailRepository bookingDetailRepo;
  RoomInventoryRepository roomInventoryRepo;
  HotelSettingService hotelSettingService;
  RoomTypeService roomTypeService;
  PaymentService paymentService;
  CancellationPolicyService cancellationPolicyService;
  BookingMapper bookingMapper;
  PaymentMapper paymentMapper;

  public Booking getById(Long id) {
    return bookingRepo
        .findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Booking"));
  }

  public Booking getByIdFetchAll(Long id) {
    return bookingRepo
        .findByIdFetchAll(id)
        .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Booking"));
  }

  @Transactional
  public CreatedBookingRes createBooking(BookingReq bookingReq, User user) {
    List<RoomBookingReq> sortedRequestedRooms =
        bookingReq.getRooms().stream()
            .sorted(Comparator.comparing(RoomBookingReq::getRoomTypeId))
            .toList();

    BigDecimal totalAmount = BigDecimal.ZERO;

    for (RoomBookingReq roomReq : sortedRequestedRooms) {
      List<RoomInventory> inventories =
          roomInventoryRepo.findAndLockByDates(
              roomReq.getRoomTypeId(), bookingReq.getCheckInDate(), bookingReq.getCheckOutDate());

      if (inventories.size() < bookingReq.getNights())
        throw new AppException(ErrorCode.INVENTORY_NOT_ENOUGH);

      for (RoomInventory inventory : inventories) {
        long availableRoom = inventory.getTotalAllotment() - inventory.getBookedCount();
        if (availableRoom < roomReq.getRoomQuantity()) {
          throw new AppException(ErrorCode.INVENTORY_NOT_ENOUGH);
        }
        inventory.setBookedCount(inventory.getBookedCount() + roomReq.getRoomQuantity());
      }
      roomInventoryRepo.saveAll(inventories);

      totalAmount =
          totalAmount.add(
              roomReq.getPrice().multiply(BigDecimal.valueOf(roomReq.getRoomQuantity())));
    }

    Booking booking = buildBookingEntity(bookingReq, user, totalAmount);
    addBookingDetail(booking, bookingReq);

    return buildBookingRes(booking);
  }

  public PageableResponse<BookingHistoryItemRes> getBookingHistory(
      PageableRequest request, String userEmail) {
    int page = request.getPage();
    int size = request.getSize();

    Pageable pageable =
        PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
    Page<Booking> bookingHistoryPage = bookingRepo.findByUserEmail(userEmail, pageable);
    List<BookingHistoryItemRes> historyItemResList =
        bookingHistoryPage.getContent().stream().map(this::buildBookingHistoryItemRes).toList();

    return PageableResponse.<BookingHistoryItemRes>builder()
        .items(historyItemResList)
        .page(page)
        .pageSize(bookingHistoryPage.getSize())
        .total(bookingHistoryPage.getTotalElements())
        .totalPages(bookingHistoryPage.getTotalPages())
        .build();
  }

  public PageableResponse<BookingItemRes> getBookingItems(DasSearchBookingReq request) {
    int page = request.getPage();
    int size = request.getSize();
    Pageable pageable =
        PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));

    Page<Booking> bookingPage = bookingRepo.findAll(buildSearchDasBookingSpec(request), pageable);
    List<BookingItemRes> bookingItemResList =
        bookingPage.getContent().stream().map(bookingMapper::toBookingItemRes).toList();

    return PageableResponse.<BookingItemRes>builder()
        .items(bookingItemResList)
        .page(page)
        .pageSize(Math.min(bookingPage.getSize(), bookingItemResList.size()))
        .total(bookingPage.getTotalElements())
        .totalPages(bookingPage.getTotalPages())
        .build();
  }

  public DasBookingDetailRes getBookingDetail(Long id) {
    Booking booking = getByIdFetchAll(id);
    String accountEmail = booking.getUser().getEmail();
    DasBookingDetailRes detail =
        bookingMapper.toDasBookingDetailRes(buildBookingHistoryItemRes(booking));

    List<PaymentHistoryRes> paymentHistory =
        booking.getPayments().stream().map(paymentMapper::toPaymentHistoryRes).toList();

    detail.setAccountEmail(accountEmail);
    detail.setPaymentHistory(paymentHistory);

    return detail;
  }

  private Booking buildBookingEntity(BookingReq bookingReq, User user, BigDecimal totalAmount) {
    Booking booking =
        Booking.builder()
            .bookingCode(BookingCodeGenerator.generateCode(8))
            .customerName(bookingReq.getCustomerName())
            .customerPhone(bookingReq.getCustomerPhone())
            .user(user)
            .checkInDate(bookingReq.getCheckInDate())
            .checkOutDate(bookingReq.getCheckOutDate())
            .totalAmount(totalAmount)
            .depositAmount(BigDecimal.ZERO)
            .balanceDue(totalAmount)
            .paymentDeadline(calculatePaymentDeadline())
            .cancellationRuleSnapshot(hotelSettingService.getCancellationPolicy().getSettingValue())
            .build();
    return bookingRepo.save(booking);
  }

  public void addBookingDetail(Booking booking, BookingReq bookingReq) {
    List<BookingDetail> bookingDetails = new ArrayList<>();
    for (RoomBookingReq roomReq : bookingReq.getRooms()) {
      RoomType roomType = roomTypeService.getReferenceById(roomReq.getRoomTypeId());
      for (int i = 0; i < roomReq.getRoomQuantity(); i++) {
        BookingDetail bookingDetail =
            BookingDetail.builder()
                .booking(booking)
                .roomType(roomType)
                .price(roomReq.getPrice())
                .adults(roomReq.getAdultsPerRoom())
                .children(roomReq.getChildrenPerRoom())
                .build();
        bookingDetails.add(bookingDetail);
      }
    }
    bookingDetailRepo.saveAll(bookingDetails);
  }

  private LocalDateTime calculatePaymentDeadline() {
    String paymentDeadlineMinutes = hotelSettingService.getPaymentDeadline().getSettingValue();
    return LocalDateTime.now().plusMinutes(Long.parseLong(paymentDeadlineMinutes));
  }

  private CreatedBookingRes buildBookingRes(Booking booking) {
    String bookingCode = booking.getBookingCode();
    String paymentUrl = paymentService.createPayment(booking);

    return CreatedBookingRes.builder().bookingCode(bookingCode).paymentUrl(paymentUrl).build();
  }

  private BookingHistoryItemRes buildBookingHistoryItemRes(Booking booking) {
    BookingHistoryItemRes history = bookingMapper.toBookingHistoryItemRes(booking);

    history.setPaidAmount(booking.getTotalAmount().subtract(booking.getBalanceDue()));

    String cancellationPolicy =
        cancellationPolicyService
            .buildCancellationPolicy(booking.getCheckInDate())
            .getDisplayText();
    history.setCancellationDisplayText(cancellationPolicy);

    Map<String, String> checkInOutPolicy = hotelSettingService.getCheckInOutPolicy();

    List<DetailHistoryItemRes> details = new ArrayList<>();
    booking
        .getBookingDetails()
        .forEach(
            detail -> {
              DetailHistoryItemRes detailItem = bookingMapper.toDetailHistoryItemRes(detail);
              details.add(detailItem);
            });
    history.setDetails(details);
    history.setCheckInTime(checkInOutPolicy.getOrDefault("check_in_time", ""));
    history.setCheckOutTime(checkInOutPolicy.getOrDefault("check_out_time", ""));

    return history;
  }

  private Specification<Booking> buildSearchDasBookingSpec(DasSearchBookingReq request) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.hasText(request.getCode())) {
        predicates.add(cb.equal(root.get("bookingCode"), request.getCode()));
      }

      if (StringUtils.hasText(request.getBookingStatus())) {
        predicates.add(cb.equal(root.get("bookingStatus"), request.getBookingStatus()));
      }

      if (StringUtils.hasText(request.getPaymentStatus())) {
        predicates.add(cb.equal(root.get("paymentStatus"), request.getPaymentStatus()));
      }

      if (request.getStartDate() != null && request.getEndDate() != null) {
        Predicate startDate =
            cb.greaterThanOrEqualTo(root.get("checkInDate"), request.getStartDate());
        Predicate endDate = cb.lessThan(root.get("checkOutDate"), request.getEndDate());
        predicates.add(cb.and(startDate, endDate));
      } else if (request.getStartDate() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("checkInDate"), request.getStartDate()));
      } else if (request.getEndDate() != null) {
        predicates.add(cb.lessThan(root.get("checkOutDate"), request.getEndDate()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
