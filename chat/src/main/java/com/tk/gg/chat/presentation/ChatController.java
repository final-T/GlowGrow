package com.tk.gg.chat.presentation;

import com.tk.gg.chat.application.dto.ChatMessageDto;
import com.tk.gg.chat.application.service.ChatApplicationService;
import com.tk.gg.chat.application.service.ChatMessageService;
import com.tk.gg.common.response.ApiUtils;
import com.tk.gg.common.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_CONTROLLER")
public class ChatController {
    private final ChatApplicationService chatApplicationService;
    private final ChatMessageService chatMessageService;

    // 채팅방 생성
    @PostMapping("/api/chat/room")
    public GlobalResponse<ChattingRoomResponseDto> createChatRoom(@RequestBody ChattingRoomCreateRequsetDto requestDto) {
        ChattingRoomResponseDto chatRoom = chatApplicationService.createChatRoom(requestDto.getUserId1(), requestDto.getUserId2());

        return ApiUtils.success("채팅방 생성 성공", chatRoom);
    }

    // 메시지 전송
    @MessageMapping("/api/ws-chat")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        chatMessageService.sendMessage(chatMessageDto);
    }
}