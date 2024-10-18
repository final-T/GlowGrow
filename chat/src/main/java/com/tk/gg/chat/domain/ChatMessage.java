package com.tk.gg.chat.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "chat_message")
public class ChatMessage {

    @Id
    private String id;

    private UUID chatRoomId;
    private Long senderId;
    private String content;
    private LocalDateTime sendAt;

    @Builder
    public ChatMessage(UUID chatRoomId, Long senderId, String content, LocalDateTime sendAt) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.sendAt = sendAt;
    }
}
