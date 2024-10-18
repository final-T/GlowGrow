package com.tk.gg.chat.domain.service;

import com.tk.gg.chat.domain.ChattingRoom;
import com.tk.gg.chat.infrastructure.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_DOMAIN_SERVICE")
public class ChatDomainService {
    private final ChattingRoomRepository chattingRoomRepository;

    // 채팅방 생성 또는 반환
    public ChattingRoom findOrCreateChatRoom(Long userId1, Long userId2, UUID reservationId) {
        log.info("채팅방 생성 또는 반환 : userId1={}, userId2={}", userId1, userId2);
        // 사용자 1과 사용자 2가 참여한 채팅방이 있는지 확인
        List<Long> userIds = List.of(userId1, userId2);
        Optional<ChattingRoom> existingChatRoom = chattingRoomRepository.findChatRoomByParticipants(userIds, (long) userIds.size());

        // 이미 존재하는 채팅방이 있다면 반환
        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get();
        } else {
            // 채팅방이 없다면 새로운 채팅방 생성
            // 예약 ID가 있다면 채팅방 생성시 함께 저장
            ChattingRoom newChatRoom = ChattingRoom.builder()
                    .reservationId(reservationId)
                    .build();

            // 채팅방 참여자 추가
            newChatRoom.addParticipant(userId1);
            newChatRoom.addParticipant(userId2);

            chattingRoomRepository.save(newChatRoom);

            return newChatRoom;
        }
    }
}
