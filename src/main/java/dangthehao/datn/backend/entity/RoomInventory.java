package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROOM_INVENTORY", schema = "HOTEL_APP")
public class RoomInventory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_TYPE_ID", nullable = false)
  private RoomType roomType;

  @NotNull
  @Column(name = "INVENTORY_DATE", nullable = false)
  private LocalDate inventoryDate;

  @ColumnDefault("0")
  @Column(name = "TOTAL_ALLOTMENT")
  private Long totalAllotment;

  @ColumnDefault("0")
  @Column(name = "BOOKED_COUNT")
  private Long bookedCount;
}
