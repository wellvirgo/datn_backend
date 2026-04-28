package dangthehao.datn.backend.service;

import dangthehao.datn.backend.dto.serviceRoomType.ServiceItemRes;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.mapper.ServiceRoomTypeMapper;
import dangthehao.datn.backend.repository.ServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ServiceRoomTypeService {
  ServiceRepository serviceRepo;
  ServiceRoomTypeMapper serviceMapper;

  public List<dangthehao.datn.backend.entity.Service> getByIds(List<Long> ids) {
    Set<Long> uniqueIds = new HashSet<>(ids);
    List<dangthehao.datn.backend.entity.Service> services =
        serviceRepo.findByIdInAndActiveTrue(uniqueIds);
    List<Long> validIds =
        services.stream().map(dangthehao.datn.backend.entity.Service::getId).toList();

    if (services.size() != ids.size()) {
      List<Long> invalidIds = ids.stream().filter(id -> !validIds.contains(id)).toList();
      throw new AppException(ErrorCode.BAD_REQUEST, "Các Service ID không hợp lệ: " + invalidIds);
    }

    return services;
  }

  public List<ServiceItemRes> getAll() {
    return serviceRepo.findAll().stream().map(serviceMapper::toServiceItemRes).toList();
  }
}
