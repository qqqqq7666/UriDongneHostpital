package com.example.elice_3rd.chat.repository;

import com.example.elice_3rd.chat.entity.mysql.ChatRoom;
import com.example.elice_3rd.chat.entity.status.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByRoomStatus(RoomStatus roomStatus);

    List<ChatRoom> findByMembers_MemberIdIn(Set<Long> memberIds);

    boolean existsByMembers_MemberId(Long memberId);
}
