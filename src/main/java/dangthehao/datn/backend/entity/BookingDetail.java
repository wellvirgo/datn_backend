package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKING_DETAILS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class BookingDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "BOOKING_ID", nullable = false)
  private Booking booking;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_TYPE_ID", nullable = false)
  private RoomType roomType;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_ID")
  private Room room;

  @NotNull
  @Column(name = "PRICE", nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  @NotNull
  @Column(name = "ADULTS", nullable = false)
  private Long adults;

  @ColumnDefault("0")
  @Column(name = "CHILDREN")
  private Long children;

  @CreatedDate
  @ColumnDefault("sysdate")
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @LastModifiedDate
  @Column(name = "UPDATE_AT")
  private LocalDate updateAt;
}
