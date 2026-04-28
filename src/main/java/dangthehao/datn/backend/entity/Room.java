package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ROOMS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_TYPE_ID")
  private RoomType roomType;

  @Size(max = 20)
  @NotNull
  @Column(name = "ROOM_NUMBER", nullable = false, length = 20)
  private String roomNumber;

  @Column(name = "FLOOR_NUMBER")
  private Short floorNumber;

  @Size(max = 50)
  @Nationalized
  @ColumnDefault("'AVAILABLE'")
  @Column(name = "STATUS", length = 50)
  private String status = "AVAILABLE";

  @Size(max = 2000)
  @Nationalized
  @Column(name = "NOTES", length = 2000)
  private String notes;

  @CreatedDate
  @ColumnDefault("sysdate")
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  private LocalDate updatedAt;
}
