package com.example.elice_3rd.notification.entity;

import com.example.elice_3rd.chat.entity.mysql.ChatRoom;
import com.example.elice_3rd.common.BaseEntity;
import com.example.elice_3rd.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiverId;

    private String senderName;

    private String chatMessageId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean readStatus = false;
}
