package com.gonjunhan.helloserver.controller;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.model.dto.ChatRequestDTO;
import com.gonjunhan.helloserver.model.vo.ChatResponseVO;
import com.gonjunhan.helloserver.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/simple")
    public Result<ChatResponseVO> chat(@RequestBody ChatRequestDTO requestDTO) {
        // 这里传入 sessionId
        String answer = chatService.chat(requestDTO.getSessionId(), requestDTO.getMessage());
        ChatResponseVO responseVO = new ChatResponseVO(requestDTO.getMessage(), answer);
        return Result.success(responseVO);
    }
}