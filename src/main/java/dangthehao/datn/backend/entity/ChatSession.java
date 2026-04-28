package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CHAT_SESSIONS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class ChatSession {
  @Id
  @Size(max = 50)
  @Column(name = "SESSION_ID", nullable = false, length = 50)
  private String sessionId;

  @CreatedDate
  @ColumnDefault("current_timestamp")
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "session")
  private Set<ChatMessage> chatMessages = new LinkedHashSet<>();
}
