package dangthehao.datn.backend.dto.user.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBaseDto {
  Long id;
  String email;
  String fullName;
  String role;
  Boolean active;
  LocalDate createdAt;
}
