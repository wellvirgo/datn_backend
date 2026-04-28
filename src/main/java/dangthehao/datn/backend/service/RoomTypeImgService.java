package dangthehao.datn.backend.service;

import dangthehao.datn.backend.entity.RoomType;
import dangthehao.datn.backend.entity.RoomTypeImg;
import dangthehao.datn.backend.repository.RoomTypeImgRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoomTypeImgService {
  RoomTypeImgRepository roomTypeImgRepo;
  FileService fileService;

  @NonFinal
  @Value("${storage.context-path}")
  String contextPath;

  @NonFinal
  @Value("${storage.paths.room-type}")
  String storagePath;

  public String saveRoomTypeThumbnail(MultipartFile file, RoomType roomType) {
    String fileName = fileService.saveFile(file, storagePath);
    String url = contextPath + storagePath + fileName;
    RoomTypeImg thumbnail =
        RoomTypeImg.builder().roomType(roomType).thumbnail(true).url(url).build();

    thumbnail = roomTypeImgRepo.save(thumbnail);
    return thumbnail.getUrl();
  }
}
