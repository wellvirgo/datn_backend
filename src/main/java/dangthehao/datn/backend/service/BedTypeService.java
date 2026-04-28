package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.bedType.response.BedTypeBaseDto;
import dangthehao.datn.backend.entity.BedType;
import dangthehao.datn.backend.mapper.BedTypeMapper;
import dangthehao.datn.backend.repository.BedTypeRepository;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BedTypeService {
  BedTypeRepository bedTypeRepo;
  BedTypeMapper bedTypeMapper;

  public BedType getReferencedBedType(Long id) {
    if (!bedTypeRepo.existsById(id))
      throw new AppException(ErrorCode.RESOURCE_NOT_FOUND, "Bed Type");

    return bedTypeRepo.getReferenceById(id);
  }

  public List<BedTypeBaseDto> getAllBedTypes() {
    return bedTypeRepo.findAllByActiveTrue().stream().map(bedTypeMapper::toBedTypeBaseDto).toList();
  }
}
