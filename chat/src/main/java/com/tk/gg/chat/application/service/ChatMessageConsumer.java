package com.tk.gg.chat.application.service;

import com.tk.gg.chat.application.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_MESSAGE_CONSUMER")
public class ChatMessageConsumer {
    private static final String CHAT_MESSAGE_TOPIC = "chat-message";

    private final SimpMessagingTemplate simpMessagingTemplate;

    // Kafka 메시지 수신, STOMP로 전달
    @KafkaListener(topics = CHAT_MESSAGE_TOPIC, groupId = "chat_group")
    public void receiveMessage(ChatMessageDto chatMessageDto) {
        log.info("메시지 수신 : {}", chatMessageDto);

        // STOMP를 통해 채팅방 구독자에게 메시지 전달
        simpMessagingTemplate.convertAndSend("/subscribe/chat/" + chatMessageDto.getChatRoomId(), chatMessageDto);
    }

}
