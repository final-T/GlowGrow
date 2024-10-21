package com.tk.gg.chat.application.service;

import com.tk.gg.chat.application.dto.ChatMessageDto;
import com.tk.gg.chat.domain.ChatMessage;
import com.tk.gg.chat.infrastructure.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_MESSAGE_SERVICE")
public class ChatMessageService {
    private static final String CHAT_MESSAGE_TOPIC = "chat-message";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ChatMessageRepository chatMessageRepository;

    // 메시지 전송 및 저장
    public void sendMessage(ChatMessageDto chatMessageDto) {
        // dto -> entity 변환 후 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(chatMessageDto.getChatRoomId())
                .senderId(chatMessageDto.getSenderId())
                .content(chatMessageDto.getContent())
                .sendAt(chatMessageDto.getSendAt())
                .build();

        // 채팅방 ID를 파티션 키로 사용하여 메시지 전송
        log.info("kafka 메시지 전송 : {}", chatMessageDto);
        kafkaTemplate.send(CHAT_MESSAGE_TOPIC, String.valueOf(chatMessageDto.getChatRoomId()), chatMessageDto);

        // 메시지 저장
        chatMessageRepository.save(chatMessage);
    }


}
