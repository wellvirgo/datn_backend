package dangthehao.datn.backend.dto.user.request;

import dangthehao.datn.backend.constant.Role;
import dangthehao.datn.backend.dto.common.PageableRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserReq extends PageableRequest {
    String email;
    String fullName;
    String phone;
    Role role;
    Boolean isActive;
}
