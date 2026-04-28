package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.ChatMessage;
import dangthehao.datn.backend.entity.ChatSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionOrderByCreatedAtDesc(ChatSession session, Pageable pageable);

    @Query("select count(*) from ChatMessage cm where cm.session.sessionId = :sessionId")
    Long countBySessionId(String sessionId);
}
