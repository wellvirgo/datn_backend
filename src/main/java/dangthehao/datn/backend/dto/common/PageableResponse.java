package dangthehao.datn.backend.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableResponse<T> {
  List<T> items;
  int page;
  int pageSize;
  long total;
  int totalPages;
}
