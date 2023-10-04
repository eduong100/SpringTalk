package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.ChatState;
import com.aus.ethan.duong.chatbot.repositories.ChatStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChatStateServiceTest {
    @Mock
    private ChatStateRepository chatStateRepository;

    @InjectMocks
    private ChatStateService chatStateService;
    private ChatState chatState;

    @BeforeEach
    public void setup() {
        chatState = ChatState
                .builder()
                .id(1L)
                .username("Ethan")
                .state("SHIFT_OPPORTUNITY_QUESTION")
                .build();
    }

    @DisplayName("Test saveChatState when newState is not in database")
    @Test
    public void givenChatState_whenSaveChatState_thenReturnChatStateObject() {
        given(chatStateRepository.findByUsername(chatState.getUsername()))
                .willReturn(Optional.empty());
        given(chatStateRepository.save(chatState)).willReturn(chatState);

        ChatState savedChatState = chatStateService.saveChatState(chatState);

        assertThat(savedChatState).isNotNull();
    }

    @DisplayName("Test saveChatState when newState is in database")
    @Test
    public void givenExistingChatState_whenSaveChatState_thenReturnChatStateObject() {
        given(chatStateRepository.findByUsername(chatState.getUsername()))
                .willReturn(Optional.of(chatState));
        given(chatStateRepository.save(chatState)).willReturn(chatState);

        ChatState savedChatState = chatStateService.saveChatState(chatState);

        assertThat(savedChatState).isNotNull();
    }

    @DisplayName("Test getChatState when username is in database")
    @Test
    public void givenChatStateUsername_whenGetChatState_thenReturnChatStateObject() {
        given(chatStateRepository.findByUsername(chatState.getUsername()))
                .willReturn(Optional.of(chatState));

        ChatState savedChatState = chatStateService.getChatState(chatState.getUsername()).get();

        assertThat(savedChatState).isNotNull();
    }

    @DisplayName("Test getChatState when username is not in database")
    @Test
    void givenEmptyChatState_whenGetChatState_thenReturnEmpty() {
        given(chatStateRepository.findByUsername(chatState.getUsername()))
                .willReturn(Optional.empty());

        boolean expected = chatStateService.getChatState(chatState.getUsername()).isEmpty();

        assertThat(expected).isTrue();
    }
}
