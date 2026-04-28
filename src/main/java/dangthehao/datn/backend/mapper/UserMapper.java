package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.user.request.UserCreateReq;
import dangthehao.datn.backend.dto.user.request.UserRegistrationReq;
import dangthehao.datn.backend.dto.user.response.CustomerItemRes;
import dangthehao.datn.backend.dto.user.response.UserBaseDto;
import dangthehao.datn.backend.dto.user.response.UserDetailRes;
import dangthehao.datn.backend.dto.user.response.UserSummaryInfo;
import dangthehao.datn.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserRegistrationReq request);

  User toUser(UserCreateReq request);

  UserBaseDto toUserBaseDto(User user);

  UserDetailRes toUserDetailRes(User user);

  UserSummaryInfo toUserSummaryInfo(User user);

  CustomerItemRes toCustomerItemRes(User user);
}
