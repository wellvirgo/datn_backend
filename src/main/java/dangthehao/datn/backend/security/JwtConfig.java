package dangthehao.datn.backend.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {
  @Value("${security.jwt.public-key}")
  private String publicKey;

  @Value("${security.jwt.private-key}")
  private String privateKey;

  @Bean
  public RSAPublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String cleanedKey =
        publicKey
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");

    byte[] encodedKey = Base64.getDecoder().decode(cleanedKey);
    KeyFactory keyFactory = getKeyFactory();
    KeySpec keySpec = new X509EncodedKeySpec(encodedKey);

    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
  }

  @Bean
  public RSAPrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String cleanedKey =
        privateKey
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

    byte[] encodedKey = Base64.getDecoder().decode(cleanedKey);
    KeyFactory keyFactory = getKeyFactory();
    KeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);

    return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
  }

  @Bean
  JwtDecoder getJwtDecoder(RSAPublicKey publicKey) {
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  @Bean
  JwtEncoder getJwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  private KeyFactory getKeyFactory() throws NoSuchAlgorithmException {
    return KeyFactory.getInstance("RSA");
  }
}
