package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.bedType.response.BedTypeBaseDto;
import dangthehao.datn.backend.entity.BedType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BedTypeMapper {
    BedTypeBaseDto toBedTypeBaseDto(BedType bedType);
}
