package com.tk.gg.chat.domain;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "p_chatting_participants")
@Table(name = "p_chatting_participants")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ChattingPartcipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chatting_participant_id")
    private UUID chattingParticipantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id", nullable = false)
    private ChattingRoom chattingRoom;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public ChattingPartcipant(ChattingRoom chattingRoom, Long userId) {
        this.chattingRoom = chattingRoom;
        this.userId = userId;
    }
}
