package com.gonjunhan.helloserver.controller;

import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.model.dto.ChatRequestDTO;
import com.gonjunhan.helloserver.model.vo.ChatResponseVO;
import com.gonjunhan.helloserver.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/simple")
    public Result<ChatResponseVO> chat(@RequestBody ChatRequestDTO requestDTO) {
        String answer = chatService.chat(requestDTO.getMessage());
        ChatResponseVO responseVO = new ChatResponseVO(requestDTO.getMessage(), answer);
        return Result.success(responseVO);
    }
}