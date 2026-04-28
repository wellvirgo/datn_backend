package dangthehao.datn.backend.dto.user.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSummaryInfo {
    Long id;
    String fullName;
    String role;
    String avatarUrl;
}
