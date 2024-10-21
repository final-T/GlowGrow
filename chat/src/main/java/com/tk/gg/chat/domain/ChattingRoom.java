package com.tk.gg.chat.domain;

import com.tk.gg.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_chatting_rooms")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ChattingRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chatting_room_id")
    private UUID chattingRoomId;

    @Column(name = "reservation_id")
    private UUID reservationId;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete = false;

    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChattingPartcipant> chattingPartcipants = new ArrayList<>();

    @Builder
    public ChattingRoom(UUID reservationId) {
        this.reservationId = reservationId;
    }


    // 채팅방에 사용자 추가
    public void addParticipant(Long userId1) {
        ChattingPartcipant chattingPartcipant = new ChattingPartcipant(this, userId1);
        chattingPartcipants.add(chattingPartcipant);
    }
}
