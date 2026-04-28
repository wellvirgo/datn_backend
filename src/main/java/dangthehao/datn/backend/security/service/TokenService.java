package dangthehao.datn.backend.security.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class TokenService {
  JwtDecoder jwtDecoder;
  JwtEncoder jwtEncoder;

  @NonFinal
  @Value("${security.jwt.access-token-alive-time}")
  Long accessTokenAliveTime;

  @NonFinal
  @Value("${security.jwt.refresh-token-alive-time}")
  Long refreshTokenAliveTime;

  public String generateToken(Authentication authentication, boolean isAccessToken) {
    String scopes = extractScopes(authentication);
    Instant issuedAt = Instant.now();
    Long aliveTime = isAccessToken ? accessTokenAliveTime : refreshTokenAliveTime;

    JwtClaimsSet claimsSet =
        JwtClaimsSet.builder()
            .issuer("DangTheHao")
            .subject(authentication.getName())
            .issuedAt(issuedAt)
            .expiresAt(issuedAt.plus(aliveTime, ChronoUnit.SECONDS))
            .claim("scope", scopes)
            .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
  }

  public String extractEmail(String token) {
    return jwtDecoder.decode(token).getSubject();
  }

  private String extractScopes(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
  }
}
