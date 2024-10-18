package com.tk.gg.chat.infrastructure.repository;

import com.tk.gg.chat.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, UUID> {
    @Query("SELECT cr FROM ChattingRoom cr " +
            "JOIN cr.chattingPartcipants cp " +
            "WHERE cp.userId IN :userIds " +
            "GROUP BY cr " +
            "HAVING COUNT(DISTINCT cp.userId) = :participantCount")
    Optional<ChattingRoom> findChatRoomByParticipants(@Param("userIds") List<Long> userIds, @Param("participantCount") Long participantCount);
}
