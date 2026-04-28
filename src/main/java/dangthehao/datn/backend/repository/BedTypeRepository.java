package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.BedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedTypeRepository extends JpaRepository<BedType, Long> {
    List<BedType> findAllByActiveTrue();
}
