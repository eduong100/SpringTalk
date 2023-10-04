package com.aus.ethan.duong.chatbot.controllers;

import com.aus.ethan.duong.chatbot.dto.ChatRequest;
import com.aus.ethan.duong.chatbot.dto.ChatResponse;
import com.aus.ethan.duong.chatbot.dto.NoMessageRequest;
import com.aus.ethan.duong.chatbot.services.ChatLogService;
import com.aus.ethan.duong.chatbot.services.DecisionTreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {
    @Mock
    ChatLogService chatLogService;

    @Mock
    DecisionTreeService decisionTreeService;

    @InjectMocks
    ChatController chatController;

    private ChatRequest chatRequest;
    private NoMessageRequest noMessageRequest;
    private ChatResponse chatResponse;

    @BeforeEach
    public void setup() {
        chatRequest = ChatRequest
                .builder()
                .message("MESSAGE")
                .username("USERNAME")
                .build();
        noMessageRequest = NoMessageRequest
                .builder()
                .username("USERNAME")
                .build();
        chatResponse = ChatResponse
                .builder()
                .message("RESPONSE")
                .build();
    }

    @DisplayName("Test handleChat")
    @Test
    public void givenRequest_whenHandleChat_returnResponse() {
        given(decisionTreeService.getResponse(chatRequest.getUsername(),chatRequest.getMessage()))
                .willReturn("RESPONSE");
        assertThat(chatController.handleChat(chatRequest)).isEqualTo(chatResponse);
    }

    @DisplayName("Test getCurrentMessage")
    @Test
    public void givenRequest_whenGetCurrentMessage_returnResponse() {
        given(decisionTreeService.getCurrentQuestion(noMessageRequest.getUsername()))
                .willReturn("RESPONSE");
        assertThat(chatController.getCurrentMessage(noMessageRequest)).isEqualTo(chatResponse);
    }

    @DisplayName("Test resetQuestionsForUser")
    @Test
    public void givenRequest_whenReset_returnResponse() {
        given(decisionTreeService.reset(noMessageRequest.getUsername()))
                .willReturn("RESPONSE");
        assertThat(chatController.resetQuestionsForUser(noMessageRequest)).isEqualTo(chatResponse);
    }
}
