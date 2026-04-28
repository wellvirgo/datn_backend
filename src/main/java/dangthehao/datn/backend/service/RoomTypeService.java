package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.common.PageableResponse;
import dangthehao.datn.backend.dto.roomType.RoomTypeImgDTO;
import dangthehao.datn.backend.dto.roomType.request.*;
import dangthehao.datn.backend.dto.roomType.response.*;
import dangthehao.datn.backend.entity.Amenity;
import dangthehao.datn.backend.entity.BedType;
import dangthehao.datn.backend.entity.RoomType;
import dangthehao.datn.backend.entity.RoomTypeImg;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.mapper.RoomTypeMapper;
import dangthehao.datn.backend.repository.RoomTypeImgRepository;
import dangthehao.datn.backend.repository.RoomTypeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoomTypeService {
  RoomTypeRepository roomTypeRepo;
  RoomTypeImgRepository roomTypeImgRepo;
  RoomTypeMapper roomTypeMapper;
  BedTypeService bedTypeService;
  AmenityService amenityService;
  ServiceRoomTypeService serviceRoomTypeService;
  FileService fileService;
  PricingEngineService pricingEngineService;
  CancellationPolicyService cancellationPolicyService;
  RoomTypeImgService roomTypeImgService;

  @NonFinal
  @Value("${storage.context-path}")
  String contextPath;

  @NonFinal
  @Value("${storage.paths.room-type}")
  String storagePath;

  public PageableResponse<RoomTypeItemRes> getRoomTypes(SearchRoomTypeReq request) {
    int page = request.getPage();
    int size = request.getSize();

    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
    Page<RoomType> result = roomTypeRepo.findAll(buildSearchSpec(request), pageable);

    Map<Long, String> thumbMap =
        getThumbnailRoomTypeMap(result.getContent().stream().map(RoomType::getId).toList());
    return PageableResponse.<RoomTypeItemRes>builder()
        .items(
            result.getContent().stream()
                .map(
                    (item) -> {
                      RoomTypeItemRes res = roomTypeMapper.toRoomTypeItemRes(item);
                      res.setThumbnail(thumbMap.get(res.getId()));
                      return res;
                    })
                .toList())
        .page(page)
        .pageSize(Math.min(result.getNumberOfElements(), size))
        .total(result.getTotalElements())
        .totalPages(result.getTotalPages())
        .build();
  }

  public PageableResponse<RoomTypeSummaryItemRes> getRoomTypeSummary(SearchRoomTypeReq request) {
    int page = request.getPage();
    int size = request.getSize();

    Pageable pageable = PageRequest.of(page - 1, size);
    Page<RoomType> result = roomTypeRepo.findAll(buildGetRoomTypeSummarySpecs(request), pageable);
    Map<Long, String> thumbMap =
        getThumbnailRoomTypeMap(result.getContent().stream().map(RoomType::getId).toList());

    return PageableResponse.<RoomTypeSummaryItemRes>builder()
        .items(
            result.getContent().stream()
                .map(
                    (item) -> {
                      RoomTypeSummaryItemRes res = roomTypeMapper.toRoomTypeSummaryItemRes(item);
                      res.setThumbnail(thumbMap.get(res.getId()));
                      return res;
                    })
                .toList())
        .page(page)
        .pageSize(Math.min(result.getNumberOfElements(), size))
        .total(result.getTotalElements())
        .totalPages(result.getTotalPages())
        .build();
  }

  @Transactional
  public RoomTypeItemRes createRoomTypes(CreateRoomTypeReq request, MultipartFile thumbFile) {
    validateRoomTypeName(request.name());
    validateCapacity(request.maxAdults(), request.baseAdults());
    validateCapacity(request.maxChildren(), request.baseChildren());

    RoomType entity = roomTypeMapper.toEntity(request);

    BedType bedType = bedTypeService.getReferencedBedType(request.bedTypeId());
    entity.setBedType(bedType);

    Short maxOccupancy = (short) (request.maxAdults() + request.maxChildren());
    entity.setMaxOccupancy(maxOccupancy);

    entity = roomTypeRepo.save(entity);
    String thumbnail = roomTypeImgService.saveRoomTypeThumbnail(thumbFile, entity);

    RoomTypeItemRes res = roomTypeMapper.toRoomTypeItemRes(entity);
    res.setThumbnail(thumbnail);

    return res;
  }

  public RoomType getById(Long id) {
    return roomTypeRepo
        .findByIdAndDeletedFalseAndActiveTrue(id)
        .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Room type"));
  }

  public RoomType getByIdEager(Long id) {
    return roomTypeRepo
        .findByIdAndDeletedFalseAndActiveTrueEager(id)
        .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Room type"));
  }

  public RoomType getReferenceById(Long id) {
    return roomTypeRepo.getReferenceById(id);
  }

  public void checkExistence(Long id) {
    boolean isExist = roomTypeRepo.existsByIdAndDeletedFalse(id);
    if (!isExist) {
      throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Room type");
    }
  }

  @Transactional
  public RoomTypeDetailRes updateBasicInfo(Long id, UpdateBasicInfoReq request) {
    validateRoomTypeName(request.getName());
    RoomType entity = getById(id);

    roomTypeMapper.updateBasicInfoRoomType(request, entity);

    entity = roomTypeRepo.save(entity);
    return roomTypeMapper.toRoomTypeDetailRes(entity);
  }

  @Transactional
  public RoomTypeDetailRes updateOccupancyRoomType(Long id, UpdateOccupancyReq request) {
    RoomType entity = getById(id);
    roomTypeMapper.updateOccupancyAndPrice(request, entity);

    validateCapacity(entity.getMaxAdults(), entity.getBaseAdults());
    validateCapacity(entity.getMaxChildren(), entity.getBaseChildren());
    entity.setMaxOccupancy((short) (entity.getMaxAdults() + entity.getMaxChildren()));

    entity = roomTypeRepo.save(entity);
    return roomTypeMapper.toRoomTypeDetailRes(entity);
  }

  @Transactional
  public RoomTypeDetailRes updateRoomSpace(Long id, UpdateRoomSpaceReq request) {
    RoomType entity = getById(id);
    roomTypeMapper.updateRoomSpace(request, entity);
    entity.setSmokingPolicy(request.getSmokingPolicy().getValue());

    BedType bedType = bedTypeService.getReferencedBedType(request.getBedTypeId());
    entity.setBedType(bedType);

    entity = roomTypeRepo.save(entity);
    return roomTypeMapper.toRoomTypeDetailRes(entity);
  }

  @Transactional
  public RoomTypeDetailRes updateRoomTypeAmenities(Long id, UpdateRoomTypeAmenitiesReq request) {
    RoomType entity = getById(id);

    List<Amenity> amenities = amenityService.getByIds(request.amenityIds());
    entity.setAmenities(new HashSet<>(amenities));

    entity = roomTypeRepo.save(entity);
    return roomTypeMapper.toRoomTypeDetailRes(entity);
  }

  @Transactional
  public RoomTypeDetailRes updateRoomTypeServices(Long id, UpdateRoomTypeServicesReq request) {
    RoomType entity = getById(id);

    List<dangthehao.datn.backend.entity.Service> services =
        serviceRoomTypeService.getByIds(request.serviceIds());
    entity.setServices(new HashSet<>(services));

    entity = roomTypeRepo.save(entity);
    return roomTypeMapper.toRoomTypeDetailRes(entity);
  }

  @Transactional
  public RoomTypeDetailRes updateRoomTypeThumb(Long id, MultipartFile file) {
    RoomType entity = getById(id);

    String fileName = fileService.saveFile(file, storagePath);
    String url = contextPath + storagePath + fileName;
    entity
        .getRoomTypeImgs()
        .forEach(
            img -> {
              if (img.getThumbnail()) img.setUrl(url);
            });

    entity = roomTypeRepo.save(entity);
    RoomTypeDetailRes roomTypeDetailRes = roomTypeMapper.toRoomTypeDetailRes(entity);
    roomTypeDetailRes.setThumbnail(url);

    return roomTypeDetailRes;
  }

  @Transactional
  public void deleteById(Long id) {
    checkExistence(id);
    roomTypeRepo.softDeletedById(id);
  }

  public CheckAvailabilityRes getAvailableRoomTypes(CheckAvailabilityReq request) {
    long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
    SearchCriteriaRes criteriaRes =
        SearchCriteriaRes.builder()
            .checkIn(request.getCheckIn())
            .checkOut(request.getCheckOut())
            .nights(nights)
            .build();

    List<AvailabilityRoomTypeRes> availabilityRoomTypes =
        roomTypeRepo.findAllAvailabilityRoomTypes(request);

    for (AvailabilityRoomTypeRes rt : availabilityRoomTypes) {
      BigDecimal finalPrice =
          pricingEngineService.calculateFinalDynamicPrice(
              rt.getId(),
              rt.getPrice(),
              request.getCheckIn(),
              rt.getTotalAllotment(),
              rt.getAvailableRoomQuantity());

      rt.setPrice(finalPrice);
    }

    return CheckAvailabilityRes.builder()
        .searchCriteria(criteriaRes)
        .cancellationPolicy(cancellationPolicyService.buildCancellationPolicy(request.getCheckIn()))
        .availableRooms(availabilityRoomTypes)
        .build();
  }

  public PublicDetailRoomTypeRes getPublicDetailRoomTypes(Long id) {
    RoomType roomType = getByIdEager(id);
    StringJoiner imgJoiner = new StringJoiner(",");
    getImgsString(roomType).values().forEach(imgJoiner::add);

    PublicDetailRoomTypeRes publicDetailRoomTypeRes =
        roomTypeMapper.toPublicDetailRoomTypeRes(roomType);
    publicDetailRoomTypeRes.setAmenities(getAmenitiesString(roomType));
    publicDetailRoomTypeRes.setServices(getServicesString(roomType));
    publicDetailRoomTypeRes.setRoomTypeImgs(imgJoiner.toString());

    return publicDetailRoomTypeRes;
  }

  public RoomTypeDetailRes getRoomTypeDetails(Long id) {
    RoomType roomType = getByIdEager(id);
    RoomTypeDetailRes detail = roomTypeMapper.toRoomTypeDetailRes(roomType);
    detail.setAmenities(getAmenitiesString(roomType));
    detail.setServices(getServicesString(roomType));

    Map<Long, String> imgMap = getImgsString(roomType);
    StringJoiner joiner = new StringJoiner(",");
    imgMap.values().forEach(joiner::add);
    detail.setThumbnail(imgMap.get(0L));
    detail.setRoomTypeImgs(joiner.toString());

    return detail;
  }

  public Long getTotalRooms() {
    return roomTypeRepo.sumTotalRooms();
  }

  public PublicDetailRoomTypeRes searchRoomTypeDetail(String keyword) {
    Long id = roomTypeRepo.findIdByClosetName(keyword);
    return getPublicDetailRoomTypes(id);
  }

  public List<String> getAllRoomTypeNames() {
    return roomTypeRepo.findAllNames();
  }

  private Specification<RoomType> buildSearchSpec(SearchRoomTypeReq request) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.hasText(request.getRoomTypeName())) {
        String nameCondition = "%" + request.getRoomTypeName().toLowerCase() + "%";
        predicates.add(cb.like(cb.lower(root.get("name")), nameCondition));
      }
      predicates.add(cb.equal(root.get("deleted"), false));

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private Specification<RoomType> buildGetRoomTypeSummarySpecs(SearchRoomTypeReq request) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.hasText(request.getRoomTypeName())) {
        String nameCondition = "%" + request.getRoomTypeName().toLowerCase() + "%";
        predicates.add(cb.like(cb.lower(root.get("name")), nameCondition));
      }
      predicates.add(cb.equal(root.get("deleted"), false));
      predicates.add(cb.equal(root.get("active"), true));

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private void validateRoomTypeName(String roomTypeName) {
    if (roomTypeRepo.existsByName(roomTypeName))
      throw new AppException(ErrorCode.DUPLICATE_RESOURCE, "Tên loại phòng");
  }

  private void validateCapacity(short capacity, short quantity) {
    if (capacity < quantity)
      throw new AppException(ErrorCode.BAD_REQUEST, "Số lượng người lớn hơn sức chứa");
  }

  private Map<Long, String> getThumbnailRoomTypeMap(List<Long> roomTypeIds) {
    return roomTypeImgRepo.findByRoomTypeIdInAndThumbnailTrue(roomTypeIds).stream()
        .collect(Collectors.toMap(RoomTypeImgDTO::getRoomTypeId, RoomTypeImgDTO::getUrl));
  }

  private String getAmenitiesString(RoomType roomType) {
    Set<Amenity> amenities = roomType.getAmenities();
    return amenities.stream().map(Amenity::getName).collect(Collectors.joining(","));
  }

  private String getServicesString(RoomType roomType) {
    Set<dangthehao.datn.backend.entity.Service> services = roomType.getServices();
    return services.stream()
        .map(dangthehao.datn.backend.entity.Service::getName)
        .collect(Collectors.joining(","));
  }

  private Map<Long, String> getImgsString(RoomType roomType) {
    Set<RoomTypeImg> roomTypeImgs = roomType.getRoomTypeImgs();
    RoomTypeImg thumbnail =
        roomTypeImgs.stream().filter(RoomTypeImg::getThumbnail).findFirst().orElse(null);

    Map<Long, String> imgMap = new HashMap<>();
    long index = 0;
    if (thumbnail != null) imgMap.put(index, thumbnail.getUrl());

    for (RoomTypeImg roomTypeImg : roomTypeImgs) {
      if (roomTypeImg.getThumbnail()) continue;
      index++;
      imgMap.put(index, roomTypeImg.getUrl());
    }

    return imgMap;
  }
}
