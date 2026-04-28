package dangthehao.datn.backend.service;

import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.util.FileUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class FileService {
  final String basePath;

  public FileService(@Value("${storage.base-path}") String basePath) {
    this.basePath = basePath;
    try {
      Files.createDirectories(getAbsolutePath(this.basePath));
    } catch (IOException e) {
      log.error("Unable to create directory at {}", this.basePath, e);
      throw new AppException(ErrorCode.IO_EXCEPTION, "Unable to create file");
    }
  }

  public String saveFile(MultipartFile file, String directory) {
    try (InputStream inputStream = file.getInputStream()) {
      String fileName = FileUtils.validateFile(file);
      String extension = FileUtils.getFileExtension(fileName);
      String newFileName = UUID.randomUUID() + extension;

      Path targetDir = Paths.get(basePath, directory);
      Files.createDirectories(targetDir);

      Path targetFile = targetDir.resolve(newFileName);
      Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);

      return newFileName;
    } catch (Exception e) {
      throw new AppException(ErrorCode.IO_EXCEPTION, "Unable to save file", e);
    }
  }

  private Path getAbsolutePath(String relativePath) {
    return Paths.get(relativePath).toAbsolutePath().normalize();
  }
}
