package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.roomType.request.CheckAvailabilityReq;
import dangthehao.datn.backend.dto.roomType.response.AvailabilityRoomTypeRes;

import java.util.List;

public interface RoomTypeCustomRepository {
  List<AvailabilityRoomTypeRes> findAllAvailabilityRoomTypes(CheckAvailabilityReq request);

  Long findIdByClosetName(String keyword);
}
