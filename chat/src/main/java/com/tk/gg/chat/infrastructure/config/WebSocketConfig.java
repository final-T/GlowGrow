package com.tk.gg.chat.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker   // WebSocket 활성화
@Slf4j(topic = "SOCKET 연결")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    private final StompHandler stompHandler;

    /**
     * WebSocket 엔드포인트 등록 및 SockJS 지원
     * @param registry StompEndpointRegistry 객체 (엔드포인트 등록) <br>
     *                 - /chat : WebSocket 연결을 위한 엔드포인트 <br>
     *                 - withSockJS() : SockJS 지원 <br>
     *                 - setAllowedOriginPatterns("*") : 모든 Origin 허용, 배포 시에는 변경 필요 <br>
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 메시지 브로커 구성
     * @param registry MessageBrokerRegistry 객체 (메시지 브로커 구성) <br>
     *                 - enableSimpleBroker("/subscribe") : /subscribe로 시작하는 메시지를 구독할 수 있도록 설정 <br>
     *                 - setApplicationDestinationPrefixes("/publish") : /publish로 시작하는 메시지를 송신할 수 있도록 설정 <br>
     *                 - /publish로 시작하는 메시지는 @MessageMapping으로 시작하는 메서드로 라우팅 <br>
     *                 - /subscribe로 시작하는 메시지는 @SendTo로 시작하는 메서드로 라우팅 <br>
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/subscribe");
        registry.setApplicationDestinationPrefixes("/publish");
    }


    /**
     * 클라이언트 인바운드 채널 구성
     * @param registration ChannelRegistration 객체 (클라이언트 채널 등록) <br>
     *                     - interceptors(stompHandler) : StompHandler를 인터셉터로 등록(인증, 권한 등의 처리) <br>
     */
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }
}
