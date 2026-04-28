package dangthehao.datn.backend.security.service;

import dangthehao.datn.backend.dto.common.AuthRequest;
import dangthehao.datn.backend.entity.RefreshToken;
import dangthehao.datn.backend.entity.User;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.repository.RefreshTokenRepository;
import dangthehao.datn.backend.repository.UserRepository;
import dangthehao.datn.backend.security.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthService {
  AuthenticationManager authenticationManager;
  TokenService tokenService;
  UserDetailsService userDetailService;
  RefreshTokenRepository refreshTokenRepo;
  UserRepository userRepo;

  @NonFinal
  @Value("${security.jwt.refresh-token-alive-time}")
  Long refreshTokenAliveTime;

  @Transactional
  public Map<String, String> authenticate(AuthRequest request) {
    Map<String, String> keyPair = new HashMap<>();
    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    String accessToken = tokenService.generateToken(auth, true);
    String refreshToken = tokenService.generateToken(auth, false);
    saveRefreshToken(refreshToken, auth);

    keyPair.put("access_token", accessToken);
    keyPair.put("refresh_token", refreshToken);

    return keyPair;
  }

  @Transactional
  public Map<String, String> renewTokenPair(String refreshToken) {
    if (!validateToken(refreshToken)) throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);

    revokeRefreshToken(refreshToken);

    Map<String, String> keyPair = new HashMap<>();

    String email = tokenService.extractEmail(refreshToken);
    UserDetails userDetails = userDetailService.loadUserByUsername(email);
    Authentication auth =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    String accessToken = tokenService.generateToken(auth, true);
    String newRefreshToken = tokenService.generateToken(auth, false);
    saveRefreshToken(newRefreshToken, auth);

    keyPair.put("access_token", accessToken);
    keyPair.put("refresh_token", newRefreshToken);
    return keyPair;
  }

  @Transactional
  public void logout(String refreshToken) {
    refreshTokenRepo.deleteByToken(refreshToken);
  }

  private void saveRefreshToken(String token, Authentication auth) {
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    User userRef = userRepo.getReferenceById(userDetails.getId());
    LocalDate expiryDate =
        LocalDate.ofInstant(
            Instant.now().plusSeconds(refreshTokenAliveTime), ZoneId.systemDefault());

    // revoked is false default
    RefreshToken refreshToken =
        RefreshToken.builder().token(token).user(userRef).expiredAt(expiryDate).build();

    refreshTokenRepo.save(refreshToken);
  }

  private boolean validateToken(String token) {
    return refreshTokenRepo.existsByTokenAndExpiredAtIsAfter(token, LocalDate.now());
  }

  private void revokeRefreshToken(String token) {
    refreshTokenRepo.deleteByToken(token);
  }
}
