package dangthehao.datn.backend.service;

import dangthehao.datn.backend.entity.ChatMessage;
import dangthehao.datn.backend.entity.ChatSession;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.repository.ChatMessageRepository;
import dangthehao.datn.backend.repository.ChatSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ChatBotService {
  ChatSessionRepository chatSessionRepo;
  ChatMessageRepository chatMessageRepo;
  ChatModel chatModel;

  @NonFinal
  @Value("${chat.ai.base-prompt}")
  String basePrompt;

  static int LIMIT_CHAT_MESSAGE = 50;

  public String sendMessageToAI(String sessionId, String message) {
    if (chatMessageRepo.countBySessionId(sessionId) >= LIMIT_CHAT_MESSAGE) {
      throw new AppException(
          ErrorCode.LIMIT_EXCEEDED, "Số lượng tin nhắn đạt tối đa. Vui lòng bắt đầu phiên mới");
    }

    ChatSession session = getChatSession(sessionId);
    saveChatMessage(session, "USER", message);

    // limit message to AI
    Pageable pageable = PageRequest.of(0, 20);
    List<ChatMessage> chatHistory =
        chatMessageRepo.findBySessionOrderByCreatedAtDesc(session, pageable);
    Collections.reverse(chatHistory);
    List<Message> promptMessages = new ArrayList<>();

    promptMessages.add(new SystemMessage(addContextToBasePrompt(basePrompt)));
    for (ChatMessage mess : chatHistory) {
      if ("USER".equals(mess.getSenderType())) {
        promptMessages.add(new UserMessage(mess.getMessageContent()));
      } else {
        promptMessages.add(new AssistantMessage(mess.getMessageContent()));
      }
    }

    Prompt prompt = new Prompt(promptMessages, buildOptions());
    ChatResponse response = chatModel.call(prompt);
    String reply = response.getResult().getOutput().getText();

    saveChatMessage(session, "AI", reply);
    return reply;
  }

  private ChatOptions buildOptions() {
    return GoogleGenAiChatOptions.builder()
        .toolNames(
            Set.of(
                "searchRoomTypeDetails",
                "getHotelSettingDetail",
                "getAllRoomTypeNames",
                "checkAvailabilityRooms"))
        .temperature(0.2)
        .build();
  }

  private String addContextToBasePrompt(String basePrompt) {
    String currentDateContext =
        "\nLƯU Ý QUAN TRỌNG: Hôm nay là "
            + LocalDate.now()
            + " ("
            + LocalDate.now().getDayOfWeek().name()
            + "). "
            + "Bạn phải dùng ngày này làm gốc để tự động tính toán mọi mốc thời gian tương đối "
            + "(như 'ngày mai', 'thứ 5 tuần sau', 'cuối tuần') "
            + "luông chuyển sang định dạng YYYY-MM-DD trước khi gọi hàm. "
            + "Không được bắt khách hàng phải tự nhập định dạng YYYY-MM-DD.";

    String formatContext =
        """
         \n[QUY TẮC TRÌNH BÀY DANH SÁCH]
          Mọi dữ liệu dạng danh sách BẮT BUỘC phải tuân thủ định dạng Markdown sau:\n
           1. Dùng dấu sao kép (**) để làm nổi bật tên đối tượng/mục chính kết hợp số thứ tự cho mỗi mục chính.
           2. Dùng dấu gạch ngang (-) cho đối tượng/mục con.
           3. Mọi thuộc tính con bên trong đối tượng đó phải được thụt lề (nested list).
           4. BẮT BUỘC xuống dòng hai lần (double line break) để phân tách giữa các đối tượng chính,\s
            giúp giao diện người dùng dễ đọc hơn.
          Ví dụ:
          **1. [Tên đối tượng/Mục chính 1]**
          - **[Tên thuộc tính 1]**: [Giá trị]
          - **[Tên thuộc tính 2]** : [Giá trị]
          \n
          **2. [Tên đối tượng/Mục chính 2]**
          - **[Tên thuộc tính 1]**: [Giá trị]
          - **[Tên thuộc tính 2]** : [Giá trị]
         """;

    String specialRules =
        "Hiện tại chưa hỗ trợ đặt phòng qua AI, không hỏi/gợi ý giúp khách hàng tự động đặt phòng";

    return basePrompt + currentDateContext + formatContext + specialRules;
  }

  private ChatSession getChatSession(String id) {
    ChatSession chatSession = chatSessionRepo.findById(id).orElse(null);
    if (chatSession == null) {
      ChatSession newChatSession = ChatSession.builder().sessionId(id).build();
      return chatSessionRepo.save(newChatSession);
    }
    return chatSession;
  }

  private void saveChatMessage(ChatSession chatSession, String senderType, String userMessage) {
    ChatMessage newMessage =
        ChatMessage.builder()
            .session(chatSession)
            .senderType(senderType)
            .messageContent(userMessage)
            .build();

    chatMessageRepo.save(newMessage);
  }
}
