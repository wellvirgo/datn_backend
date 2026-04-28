package dangthehao.datn.backend.service;

import dangthehao.datn.backend.constant.Role;
import dangthehao.datn.backend.dto.common.PageableResponse;
import dangthehao.datn.backend.dto.user.request.SearchUserReq;
import dangthehao.datn.backend.dto.user.request.UserCreateReq;
import dangthehao.datn.backend.dto.user.request.UserRegistrationReq;
import dangthehao.datn.backend.dto.user.response.CustomerItemRes;
import dangthehao.datn.backend.dto.user.response.UserBaseDto;
import dangthehao.datn.backend.dto.user.response.UserDetailRes;
import dangthehao.datn.backend.dto.user.response.UserSummaryInfo;
import dangthehao.datn.backend.entity.User;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.mapper.UserMapper;
import dangthehao.datn.backend.repository.BookingRepository;
import dangthehao.datn.backend.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
  UserRepository userRepo;
  BookingRepository bookingRepo;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  FileService fileService;

  @NonFinal
  @Value("${storage.context-path}")
  String contextPath;

  @NonFinal
  @Value("${storage.paths.user}")
  String userImagePath;

  public Long register(UserRegistrationReq request) {
    isEmailExist(request.getEmail());
    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setAvatarUrl("http://localhost:8080/users/default-avatar.svg");
    user = userRepo.save(user);
    return user.getId();
  }

  public Long createUser(UserCreateReq request) {
    isEmailExist(request.getEmail());
    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setAvatarUrl("http://localhost:8080/users/default-avatar.svg");
    user = userRepo.save(user);
    return user.getId();
  }

  public User getUserByEmail(String email) {
    return userRepo
        .findByEmailAndDeletedFalse(email)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }

  public UserSummaryInfo getUserSummaryInfo(String email) {
    User user = getUserByEmail(email);
    return userMapper.toUserSummaryInfo(user);
  }

  public PageableResponse<UserBaseDto> getAllUsers(boolean isReceptionist, SearchUserReq request) {
    int page = request.getPage();
    int size = request.getSize();
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<User> userPage = userRepo.findAll(buildSearchCriteria(isReceptionist, request), pageable);
    List<UserBaseDto> userDtos =
        userPage.getContent().stream().map(userMapper::toUserBaseDto).toList();

    return PageableResponse.<UserBaseDto>builder()
        .items(userDtos)
        .page(page)
        .pageSize(Math.min(size, userDtos.size()))
        .total(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  public PageableResponse<CustomerItemRes> getAllCustomer(
      boolean isReceptionist, SearchUserReq request) {
    int page = request.getPage();
    int size = request.getSize();
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));

    request.setRole(Role.CUSTOMER);
    Page<User> userPage = userRepo.findAll(buildSearchCriteria(isReceptionist, request), pageable);
    List<CustomerItemRes> customerItemRes =
        userPage.stream()
            .map(
                user -> {
                  CustomerItemRes customerItem = this.userMapper.toCustomerItemRes(user);
                  Long totalBookings = bookingRepo.countByUserId(user.getId());
                  BigDecimal totalSpent =
                      bookingRepo.calculateTotalSpentByUserId(user.getId()) != null
                          ? bookingRepo.calculateTotalSpentByUserId(user.getId())
                          : BigDecimal.ZERO;

                  customerItem.setTotalBooking(totalBookings);
                  customerItem.setTotalSpent(totalSpent);

                  return customerItem;
                })
            .toList();

    return PageableResponse.<CustomerItemRes>builder()
        .items(customerItemRes)
        .page(page)
        .pageSize(Math.min(size, customerItemRes.size()))
        .total(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .build();
  }

  public UserDetailRes getUserDetail(Authentication auth, Long id) {
    User user = getUserById(id);

    String currentEmail = auth.getName();
    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

    if (isCustomer(authorities)) {
      if (!user.getEmail().equals(currentEmail)) throw new AppException(ErrorCode.FORBIDDEN);
    } else if (isReceptionist(authorities)) {
      if (!user.getEmail().equals(currentEmail) && !"CUSTOMER".equals(user.getRole()))
        throw new AppException(ErrorCode.FORBIDDEN);
    }

    return userMapper.toUserDetailRes(user);
  }

  public String updateAvatar(String currUserEmail, Long id, MultipartFile file) {
    User user = getUserById(id);

    if (!user.getEmail().equals(currUserEmail)) {
      throw new AppException(ErrorCode.FORBIDDEN);
    }

    String fileName = fileService.saveFile(file, userImagePath);
    String avatarUrl = contextPath + userImagePath + fileName;
    user.setAvatarUrl(avatarUrl);
    user = userRepo.save(user);

    return user.getAvatarUrl();
  }

  public void deleteUser(Long userId) {
    User user = getUserById(userId);
    user.setDeleted(true);
    userRepo.save(user);
  }

  private User getUserById(Long userId) {
    return userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }

  private void isEmailExist(String email) {
    if (userRepo.existsByEmail(email))
      throw new AppException(ErrorCode.DUPLICATE_RESOURCE, "Email");
  }

  private boolean isCustomer(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream().allMatch(role -> "ROLE_CUSTOMER".equals(role.getAuthority()));
  }

  private boolean isReceptionist(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream().allMatch(role -> "ROLE_RECEPTIONIST".equals(role.getAuthority()));
  }

  private Specification<User> buildSearchCriteria(boolean isReceptionist, SearchUserReq request) {
    return ((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(cb.equal(root.get("deleted"), false));
      if (isReceptionist) {
        predicates.add(cb.equal(root.get("role"), Role.CUSTOMER.name()));
      }

      if (StringUtils.hasText(request.getEmail())) {
        predicates.add(cb.equal(root.get("email"), request.getEmail()));
      }

      if (StringUtils.hasText(request.getFullName())) {
        predicates.add(cb.equal(root.get("fullName"), request.getFullName()));
      }

      if (StringUtils.hasText(request.getPhone())) {
        predicates.add(cb.equal(root.get("phone"), request.getPhone()));
      }

      if (!isReceptionist && request.getRole() != null) {
        predicates.add(cb.equal(root.get("role"), request.getRole().name()));
      }

      if (request.getIsActive() != null) {
        predicates.add(cb.equal(root.get("active"), request.getIsActive()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    });
  }
}
