package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.roomType.request.CreateRoomTypeReq;
import dangthehao.datn.backend.dto.roomType.request.UpdateBasicInfoReq;
import dangthehao.datn.backend.dto.roomType.request.UpdateOccupancyReq;
import dangthehao.datn.backend.dto.roomType.request.UpdateRoomSpaceReq;
import dangthehao.datn.backend.dto.roomType.response.PublicDetailRoomTypeRes;
import dangthehao.datn.backend.dto.roomType.response.RoomTypeDetailRes;
import dangthehao.datn.backend.dto.roomType.response.RoomTypeItemRes;
import dangthehao.datn.backend.dto.roomType.response.RoomTypeSummaryItemRes;
import dangthehao.datn.backend.entity.RoomType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {
  @Mapping(target = "bedTypeId", source = "bedType.id")
  @Mapping(target = "bedTypeName", source = "bedType.name")
  RoomTypeItemRes toRoomTypeItemRes(RoomType roomType);

  @Mapping(target = "smokingPolicy", source = "smokingPolicy.value")
  RoomType toEntity(CreateRoomTypeReq createRoomTypeReq);

  @Mapping(target = "amenities", ignore = true)
  @Mapping(target = "services", ignore = true)
  @Mapping(target = "roomTypeImgs", ignore = true)
  @Mapping(target = "bedTypeId", source = "bedType.id")
  @Mapping(target = "bedTypeName", source = "bedType.name")
  RoomTypeDetailRes toRoomTypeDetailRes(RoomType roomType);

  @Mapping(target = "bedTypeName", source = "bedType.name")
  RoomTypeSummaryItemRes toRoomTypeSummaryItemRes(RoomType roomType);

  @Mapping(target = "amenities", ignore = true)
  @Mapping(target = "services", ignore = true)
  @Mapping(target = "roomTypeImgs", ignore = true)
  @Mapping(target = "bedTypeName", source = "bedType.name")
  PublicDetailRoomTypeRes toPublicDetailRoomTypeRes(RoomType roomType);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateBasicInfoRoomType(UpdateBasicInfoReq dto, @MappingTarget RoomType roomType);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateOccupancyAndPrice(UpdateOccupancyReq dto, @MappingTarget RoomType roomType);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateRoomSpace(UpdateRoomSpaceReq dto, @MappingTarget RoomType roomType);
}
