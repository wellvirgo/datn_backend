package dangthehao.datn.backend.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${storage.base-path}")
  String basePath;

  @Value("${storage.paths.room-type}")
  String roomTypePath;

  @Value("${storage.paths.user}")
  String userPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/room-types/**")
        .addResourceLocations("file:" + basePath + "/" + roomTypePath);

    registry
        .addResourceHandler("/users/**")
        .addResourceLocations("file:" + basePath + "/" + userPath);
  }
}
