package com.tk.gg.common.kafka.alarm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Getter
@JsonSerialize
@JsonDeserialize
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationDto {
    Long userId;
    String message;
    String type; // promotion, reservation, payment, post, chat
}
