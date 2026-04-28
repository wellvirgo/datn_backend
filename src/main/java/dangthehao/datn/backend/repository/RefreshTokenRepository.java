package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  boolean existsByTokenAndExpiredAtIsAfter(String token, LocalDate now);
  void deleteByToken(String token);
}
