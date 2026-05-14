package com.gonjunhan.helloserver.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.gonjunhan.helloserver.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final StringRedisTemplate redisTemplate;  // 注入Redis

    // 构造方法同时注入 ChatClient 和 Redis
    public ChatServiceImpl(ChatClient.Builder chatClientBuilder, StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;  // 赋值
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一名专业的智能助手")
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withModel("qwen-turbo")
                                .withTopP(0.7)
                                .build()
                )
                .build();
    }

    // ====================== 核心：带记忆的聊天方法 ======================
    @Override
    public String chat(String sessionId, String message) {
        // 1. 拼接Redis的key
        String key = "chat:session:" + sessionId;

        // 2. 获取历史对话
        String history = redisTemplate.opsForValue().get(key);
        if (history == null) history = "";

        // 3. 拼接提示词（历史 + 当前问题）
        String fullPrompt = history + "\n用户：" + message + "\nAI：";

        // 4. 调用AI
        String reply = chatClient.prompt().user(fullPrompt).call().content();

        // 5. 生成新的历史记录（只保留最近3轮）
        String newHistory = (history + "\n用户：" + message + "\nAI：" + reply).trim();
        String[] lines = newHistory.split("\n");
        if (lines.length > 6) {
            newHistory = String.join("\n", Arrays.copyOfRange(lines, lines.length - 6, lines.length));
        }

        // 6. 存回Redis
        redisTemplate.opsForValue().set(key, newHistory);

        return reply;
    }
}