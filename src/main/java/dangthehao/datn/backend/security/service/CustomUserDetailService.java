package dangthehao.datn.backend.security.service;

import dangthehao.datn.backend.entity.User;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.repository.UserRepository;
import dangthehao.datn.backend.security.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CustomUserDetailService implements UserDetailsService {
  UserRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepo.findByEmailAndDeletedFalse(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    return new CustomUserDetails(
        user.getId(), user.getEmail(), user.getPassword(), getAuthorities(user));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    return Arrays.stream(user.getRole().split("-"))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }
}
