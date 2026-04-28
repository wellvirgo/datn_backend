package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ROOM_TYPES", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class RoomType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 512)
  @NotNull
  @Nationalized
  @Column(name = "NAME", nullable = false, length = 512)
  private String name;

  @ColumnDefault("2")
  @Column(name = "BASE_ADULTS")
  private Short baseAdults;

  @ColumnDefault("0")
  @Column(name = "BASE_CHILDREN")
  private Short baseChildren;

  @NotNull
  @Column(name = "MAX_ADULTS", nullable = false)
  private Short maxAdults;

  @NotNull
  @Column(name = "MAX_CHILDREN", nullable = false)
  private Short maxChildren;

  @NotNull
  @Column(name = "MAX_OCCUPANCY", nullable = false)
  private Short maxOccupancy;

  @ColumnDefault("0")
  @Column(name = "AREA_SIZE", precision = 8, scale = 2)
  private BigDecimal areaSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "BED_TYPE_ID")
  private BedType bedType;

  @Size(max = 1024)
  @Nationalized
  @Column(name = "BED_ARRANGEMENT", length = 1024)
  private String bedArrangement;

  @Size(max = 50)
  @Nationalized
  @Column(name = "VIEW_TYPE", length = 50)
  private String viewType;

  @ColumnDefault("false")
  @Column(name = "EXTRA_BED_ALLOWED")
  private Boolean extraBedAllowed = false;

  @NotNull
  @Column(name = "BASE_PRICE", nullable = false, precision = 12, scale = 2)
  private BigDecimal basePrice;

  @Column(name = "EXTRA_BED_PRICE", precision = 12, scale = 2)
  private BigDecimal extraBedPrice;

  @Size(max = 2000)
  @Nationalized
  @Column(name = "DESCRIPTION", length = 2000)
  private String description;

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
  @Column(name = "UPDATED_AT")
  private LocalDate updatedAt;

  @Size(max = 512)
  @Nationalized
  @ColumnDefault("'Not in room'")
  @Column(name = "SMOKING_POLICY", length = 512)
  private String smokingPolicy = "Not in room";

  @Size(max = 512)
  @Nationalized
  @Column(name = "BATHROOM_TYPE", length = 512)
  private String bathroomType;

  @ManyToMany
  @JoinTable(
      name = "ROOM_TYPE_AMENITY",
      joinColumns = @JoinColumn(name = "ROOM_TYPE_ID"),
      inverseJoinColumns = @JoinColumn(name = "AMENITY_ID"))
  private Set<Amenity> amenities = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(
      name = "ROOM_TYPE_SERVICES",
      joinColumns = @JoinColumn(name = "ROOM_TYPE_ID"),
      inverseJoinColumns = @JoinColumn(name = "SERVICE_ID"))
  private Set<Service> services = new LinkedHashSet<>();

  @OneToMany(mappedBy = "roomType")
  private Set<RoomTypeImg> roomTypeImgs = new LinkedHashSet<>();

  @NotNull
  @ColumnDefault("1")
  @Column(name = "TOTAL_ROOMS", nullable = false)
  private Long totalRooms = 1L;
}
