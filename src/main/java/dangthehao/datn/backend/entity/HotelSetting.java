package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "HOTEL_SETTINGS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class HotelSetting {
  @Id
  @Size(max = 100)
  @Column(name = "SETTING_KEY", nullable = false, length = 100)
  private String settingKey;

  @Size(max = 255)
  @NotNull
  @Column(name = "SETTING_VALUE", nullable = false)
  private String settingValue;

  @Size(max = 2000)
  @Nationalized
  @Column(name = "DESCRIPTION", length = 2000)
  private String description;

  @ColumnDefault("true")
  @Column(name = "IS_PUBLIC")
  private Boolean isPublic;

  @CreatedDate
  @ColumnDefault("sysdate")
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @LastModifiedDate
  @Column(name = "UPDATED_AT")
  private LocalDate updatedAt;
}
