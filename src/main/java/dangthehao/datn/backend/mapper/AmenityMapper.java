package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.amenity.AmenityItemRes;
import dangthehao.datn.backend.entity.Amenity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    AmenityItemRes toAmenityItemRes(Amenity amenity);
}
