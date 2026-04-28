package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CHAT_MESSAGES", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MESSAGE_ID", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "SESSION_ID", nullable = false)
  private ChatSession session;

  @Size(max = 10)
  @Column(name = "SENDER_TYPE", length = 10)
  private String senderType;

  @NotNull
  @Lob
  @Column(name = "MESSAGE_CONTENT", nullable = false)
  private String messageContent;

  @CreatedDate
  @ColumnDefault("current_timestamp")
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;
}
