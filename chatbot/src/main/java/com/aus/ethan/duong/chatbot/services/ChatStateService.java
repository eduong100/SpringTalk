package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.ChatState;
import com.aus.ethan.duong.chatbot.repositories.ChatStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatStateService {
    private final ChatStateRepository chatStateRepository;

    /**
     * Upsert new chat state
     * @param savedChatState {ChatState}
     * @return {ChatState}
     */
    public ChatState saveChatState(ChatState savedChatState) {
        Optional<ChatState> optionChatState = chatStateRepository
                .findByUsername(savedChatState.getUsername());

        if (optionChatState.isEmpty()) {
            return chatStateRepository.save(savedChatState);
        }

        ChatState repositoryChatState = optionChatState.get();
        repositoryChatState.setState(savedChatState.getState());
        return chatStateRepository.save(repositoryChatState);
    }

    /**
     * Find state by username
     * @param username {String}
     * @return {Optional ChatState}
     */
    public Optional<ChatState> getChatState(String username) {
        return chatStateRepository.findByUsername(username);
    }

}
