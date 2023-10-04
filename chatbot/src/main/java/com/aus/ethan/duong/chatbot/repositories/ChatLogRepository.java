package com.aus.ethan.duong.chatbot.repositories;

import com.aus.ethan.duong.chatbot.models.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
}
