package dangthehao.datn.backend.util;

import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
  public static String validateFile(MultipartFile file) {
    if (!hasContent(file)) throw new AppException(ErrorCode.BAD_REQUEST, "File is empty");

    String fileName = file.getOriginalFilename();
    if (!isValidFilename(fileName))
      throw new AppException(ErrorCode.BAD_REQUEST, "File name is invalid");

    return fileName;
  }

  public static boolean hasContent(MultipartFile file) {
    return file != null && !file.isEmpty();
  }

  public static boolean isValidFilename(String filename) {
    if (!StringUtils.hasText(filename)) return false;
    return !filename.contains("..");
  }

  public static String getFileExtension(String fileName) {
    int lastDotIdx = fileName != null ? fileName.lastIndexOf(".") : -1;
    if (lastDotIdx == -1 || lastDotIdx == fileName.length() - 1)
      throw new AppException(ErrorCode.BAD_REQUEST, "File extension is invalid");

    return fileName.substring(lastDotIdx);
  }
}
