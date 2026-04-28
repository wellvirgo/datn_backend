package dangthehao.datn.backend.dto.user.request;

import dangthehao.datn.backend.constant.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateReq extends UserRegistrationReq {
    @NotNull(message = "Phải chỉ định role cho user")
    Role role;
}
