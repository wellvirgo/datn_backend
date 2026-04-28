package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.roomType.request.CheckAvailabilityReq;
import dangthehao.datn.backend.dto.roomType.response.AvailabilityRoomTypeRes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
public class RoomTypeCustomRepositoryImpl implements RoomTypeCustomRepository {
  EntityManager em;

  @Override
  public List<AvailabilityRoomTypeRes> findAllAvailabilityRoomTypes(CheckAvailabilityReq request) {
    StoredProcedureQuery query =
        em.createStoredProcedureQuery("check_available_room_type".toUpperCase());

    query.registerStoredProcedureParameter("check_in", LocalDate.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("check_out", LocalDate.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

    query.setParameter("check_in", request.getCheckIn());
    query.setParameter("check_out", request.getCheckOut());

    query.execute();

    List<Object[]> results = query.getResultList();
    List<AvailabilityRoomTypeRes> res = new ArrayList<>();

    for (Object[] row : results) {
      AvailabilityRoomTypeRes resObj =
          AvailabilityRoomTypeRes.builder()
              .id(row[0] != null ? ((Number) row[0]).longValue() : null)
              .name((String) row[1])
              .viewType((String) row[2])
              .areaSize(row[3] != null ? new BigDecimal(row[3].toString()) : null)
              .maxAdults(row[4] != null ? ((Number) row[4]).shortValue() : null)
              .maxChildren(row[5] != null ? ((Number) row[5]).shortValue() : null)
              .maxOccupancy(row[6] != null ? ((Number) row[6]).shortValue() : null)
              .bedArrangement((String) row[7])
              .extraBedAllowed(Boolean.valueOf(row[8].toString()))
              .extraBedPrice(row[9] != null ? new BigDecimal(row[9].toString()) : null)
              .availableRoomQuantity(row[10] != null ? ((Number) row[10]).longValue() : null)
              .bedTypeName((String) row[11])
              .amenities((String) row[12])
              .thumbnail((String) row[13])
              .description((String) row[14])
              .baseAdults(row[15] != null ? ((Number) row[15]).shortValue() : null)
              .baseChildren(row[16] != null ? ((Number) row[16]).shortValue() : null)
              .price(row[17] != null ? new BigDecimal(row[17].toString()) : null)
              .totalAllotment(row[18] != null ? ((Number) row[18]).longValue() : null)
              .build();

      res.add(resObj);
    }

    return res;
  }

  @Override
  public Long findIdByClosetName(String keyword) {
    StoredProcedureQuery query = em.createStoredProcedureQuery("find_closet_rt_name".toUpperCase());

    query.registerStoredProcedureParameter("p_keyword", String.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_min_score", Integer.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_room_type_id", Long.class, ParameterMode.OUT);

    query.setParameter("p_keyword", keyword);
    query.setParameter("p_min_score", 65);

    query.execute();

    return query.getOutputParameterValue("p_room_type_id") != null
        ? ((Number) query.getOutputParameterValue("p_room_type_id")).longValue()
        : null;
  }
}
