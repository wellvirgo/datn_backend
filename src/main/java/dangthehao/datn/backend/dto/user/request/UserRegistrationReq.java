package dangthehao.datn.backend.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegistrationReq {
  @Size(max = 255)
  @NotNull(message = "Tên không được để trống")
  String fullName;

  @Size(max = 255)
  @NotNull(message = "Email không được để trống")
  @Email
  String email;

  @Size(max = 255)
  @NotNull(message = "Yêu cầu nhập mật khẩu")
  String password;
}
