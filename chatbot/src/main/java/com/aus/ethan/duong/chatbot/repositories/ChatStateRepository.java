package com.aus.ethan.duong.chatbot.repositories;

import com.aus.ethan.duong.chatbot.models.ChatState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatStateRepository extends JpaRepository<ChatState, Long> {
    Optional<ChatState> findByUsername(String username);
}
