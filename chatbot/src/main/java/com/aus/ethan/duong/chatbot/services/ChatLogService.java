package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.ChatLog;
import com.aus.ethan.duong.chatbot.repositories.ChatLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatLogService {
    private final ChatLogRepository chatLogRepository;

    /**
     * Save log
     * @param chatLog {ChatLog}
     * @return {ChatLog}
     */
    public ChatLog saveChatLog(ChatLog chatLog) {
        return chatLogRepository.save(chatLog);
    }
}
