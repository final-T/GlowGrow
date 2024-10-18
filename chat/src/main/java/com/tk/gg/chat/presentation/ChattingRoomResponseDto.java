package com.tk.gg.chat.presentation;

import lombok.Data;

import java.util.UUID;

@Data
public class ChattingRoomResponseDto {
    private UUID chatRoomId;

    public ChattingRoomResponseDto(UUID chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
