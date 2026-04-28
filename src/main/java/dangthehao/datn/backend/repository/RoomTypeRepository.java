package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository
    extends JpaRepository<RoomType, Long>,
        JpaSpecificationExecutor<RoomType>,
        RoomTypeCustomRepository {
  boolean existsByName(String name);

  boolean existsByIdAndDeletedFalse(Long id);

  @NonNull
  @EntityGraph(attributePaths = {"bedType"})
  Page<RoomType> findAll(Specification<RoomType> spec, Pageable pageable);

  Optional<RoomType> findByIdAndDeletedFalseAndActiveTrue(Long id);

  @EntityGraph(attributePaths = {"amenities", "services", "roomTypeImgs", "bedType"})
  @Query(
      "select rt from RoomType rt where rt.id = :id and rt.deleted = false and rt.active = true ")
  Optional<RoomType> findByIdAndDeletedFalseAndActiveTrueEager(Long id);

  @Modifying
  @Query("update RoomType rt set rt.deleted = true where rt.id = :id")
  void softDeletedById(Long id);

  List<RoomType> findAllByDeletedFalseAndActiveTrue();

  @Query("select rt.basePrice from RoomType rt where rt.id = :id")
  Optional<BigDecimal> findBasePriceById(Long id);

 @Query("select sum(rt.totalRooms) from RoomType rt where rt.deleted = false")
  Long sumTotalRooms();

 @Query("select rt.name from RoomType rt where rt.deleted = false and rt.active = true")
 List<String> findAllNames();
}
