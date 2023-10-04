package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.ChatLog;
import com.aus.ethan.duong.chatbot.repositories.ChatLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatLogServiceTest {
    @Mock
    private ChatLogRepository chatLogRepository;

    @InjectMocks
    private ChatLogService chatLogService;
    private ChatLog chatLog;

    @BeforeEach
    public void setup() {
        chatLog = ChatLog
                .builder()
                .id(1L)
                .message("Message")
                .response("Response")
                .username("Ethan")
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @DisplayName("Test saveChatLog")
    @Test
    public void givenLogObject_whenSaveChatLog_returnLogObject() {
        given(chatLogRepository.save(chatLog)).willReturn(chatLog);

        ChatLog savedChatLog = chatLogService.saveChatLog(chatLog);

        assertThat(savedChatLog).isNotNull();
    }
}
