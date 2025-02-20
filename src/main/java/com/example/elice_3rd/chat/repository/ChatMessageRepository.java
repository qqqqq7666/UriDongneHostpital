package com.example.elice_3rd.chat.repository;

import com.example.elice_3rd.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

    Flux<ChatMessage> findByChatRoomIdAndCreatedDateAfter(Long chatRoomId, LocalDateTime createdDate);

    Flux<ChatMessage> findByChatRoomId(Long chatRoomId);

    Mono<Void> deleteByChatRoomId(Long chatRoomId);
}
