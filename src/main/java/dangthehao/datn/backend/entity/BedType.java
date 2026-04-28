package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "BED_TYPES", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class BedType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 20)
  @NotNull
  @Column(name = "CODE", nullable = false, length = 20)
  private String code;

  @Size(max = 100)
  @NotNull
  @Nationalized
  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @ColumnDefault("TRUE")
  @Column(name = "ACTIVE")
  private Boolean active = true;

  @CreatedDate
  @ColumnDefault("sysdate")
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  private LocalDate updatedAt;
}
