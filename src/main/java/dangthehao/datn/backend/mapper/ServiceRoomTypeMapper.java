package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.serviceRoomType.ServiceItemRes;
import dangthehao.datn.backend.entity.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceRoomTypeMapper {
  ServiceItemRes toServiceItemRes(Service service);
}
