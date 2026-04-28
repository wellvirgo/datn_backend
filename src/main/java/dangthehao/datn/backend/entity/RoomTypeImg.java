package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROOM_TYPE_IMG", schema = "HOTEL_APP")
public class RoomTypeImg {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_TYPE_ID")
  private RoomType roomType;

  @Size(max = 1024)
  @NotNull
  @Column(name = "URL", nullable = false, length = 1024)
  private String url;

  @ColumnDefault("false")
  @Column(name = "THUMBNAIL")
  private Boolean thumbnail;
}
