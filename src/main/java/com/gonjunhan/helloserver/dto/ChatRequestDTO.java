package com.gonjunhan.helloserver.model.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String sessionId;  // 新增这个
    private String message;
}