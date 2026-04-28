package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.roomType.RoomTypeImgDTO;
import dangthehao.datn.backend.entity.RoomTypeImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoomTypeImgRepository
    extends JpaRepository<RoomTypeImg, Long> {
  @Transactional(readOnly = true)
  List<RoomTypeImgDTO> findByRoomTypeIdInAndThumbnailTrue(List<Long> roomTypeIds);
}
