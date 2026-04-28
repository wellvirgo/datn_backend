package dangthehao.datn.backend.dto.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableRequest {
  Integer page;
  Integer size;

  public int getPage() {
    return page == null ? 1 : page;
  }

  public int getSize() {
    return size == null ? 10 : size;
  }
}
