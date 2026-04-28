package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.amenity.AmenityItemRes;
import dangthehao.datn.backend.entity.Amenity;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.mapper.AmenityMapper;
import dangthehao.datn.backend.repository.AmenityRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AmenityService {
  AmenityRepository amenityRepo;
  AmenityMapper amenityMapper;

  public List<Amenity> getByIds(List<Long> amenityIds) {
    Set<Long> uniqueAmenityIds = new HashSet<>(amenityIds);

    List<Amenity> amenities = amenityRepo.findAllByIdInAndActiveTrue(uniqueAmenityIds);
    List<Long> validAmenityIds = amenities.stream().map(Amenity::getId).toList();
    if (amenities.size() != amenityIds.size()) {
      List<Long> invalidIds =
          uniqueAmenityIds.stream().filter(id -> !validAmenityIds.contains(id)).toList();
      throw new AppException(ErrorCode.BAD_REQUEST, "Các Amenity ID không hợp lệ: " + invalidIds);
    }

    return amenities;
  }

  public List<AmenityItemRes> getAll() {
    return amenityRepo.findAllByActiveTrue().stream().map(amenityMapper::toAmenityItemRes).toList();
  }
}
