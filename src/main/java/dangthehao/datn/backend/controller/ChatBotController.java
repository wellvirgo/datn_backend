package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.service.ChatBotService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatBotController {
  ChatBotService chatBotService;

  @PostMapping("/{sessionId}")
  public ResponseEntity<ApiResponse<Map<String, String>>> chat(
      @PathVariable String sessionId, @RequestBody Map<String, String> request) {
    String result = chatBotService.sendMessageToAI(sessionId, request.get("message"));

    return ResponseEntity.ok(ApiResponseBuilder.success(Map.of("reply", result)));
  }
}
