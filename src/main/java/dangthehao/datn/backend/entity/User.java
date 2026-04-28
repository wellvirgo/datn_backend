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
@Table(name = "USERS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 255)
  @NotNull
  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Size(max = 255)
  @NotNull
  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @Size(max = 512)
  @NotNull
  @Nationalized
  @Column(name = "FULL_NAME", nullable = false, length = 512)
  private String fullName;

  @Size(max = 20)
  @Column(name = "PHONE", length = 20)
  private String phone;

  @Size(max = 50)
  @NotNull
  @ColumnDefault("'CUSTOMER'")
  @Column(name = "ROLE", nullable = false, length = 50)
  private String role = "CUSTOMER";

  @Size(max = 512)
  @Column(name = "AVATAR_URL", length = 512)
  private String avatarUrl;

  @ColumnDefault("true")
  @Column(name = "ACTIVE")
  private Boolean active = true;

  @ColumnDefault("false")
  @Column(name = "DELETED")
  private Boolean deleted = false;

  @CreatedDate
  @ColumnDefault("SYSDATE")
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @LastModifiedDate
  @Column(name = "UPDATE_AT")
  private LocalDate updateAt;
}
