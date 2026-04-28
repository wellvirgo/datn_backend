package dangthehao.datn.backend.dto.roomType.request;

import dangthehao.datn.backend.dto.common.PageableRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRoomTypeReq extends PageableRequest {
    String roomTypeName;
}
