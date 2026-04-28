package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findByEmailAndDeletedFalse(String email);

  boolean existsByEmail(@Size(max = 255) @NotNull String email);

  @NonNull
  Page<User> findAll(Specification<User> spec, Pageable pageable);
}
