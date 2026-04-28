package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
  List<Amenity> findAllByIdInAndActiveTrue(Collection<Long> amenityIds);

  List<Amenity> findAllByActiveTrue();
}
