package com.tk.gg.chat.application.service;

import com.tk.gg.chat.presentation.ChattingRoomResponseDto;
import com.tk.gg.chat.domain.ChattingRoom;
import com.tk.gg.chat.domain.service.ChatDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_APPLICATION_SERVICE")
public class ChatApplicationService {
    private final ChatDomainService chatDomainService;

    // 채팅방 생성
    public ChattingRoomResponseDto createChatRoom(Long userId1, Long userId2) {
        log.info("채팅방 생성 : userId1={}, userId2={}", userId1, userId2);

        // TODO: reservationId 가져오는 로직 추가
        ChattingRoom createChatRoom = chatDomainService.findOrCreateChatRoom(userId1, userId2, null);

        // entity -> dto 변환 후 반환
        return new ChattingRoomResponseDto(createChatRoom.getChattingRoomId());
    }

}
