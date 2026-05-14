package com.gonjunhan.helloserver.service;

public interface ChatService {
    // 旧的删掉，换成这个带 sessionId 的
    String chat(String sessionId, String message);
}