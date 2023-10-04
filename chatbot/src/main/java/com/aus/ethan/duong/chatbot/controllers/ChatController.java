package com.aus.ethan.duong.chatbot.controllers;

import com.aus.ethan.duong.chatbot.dto.ChatRequest;
import com.aus.ethan.duong.chatbot.dto.ChatResponse;
import com.aus.ethan.duong.chatbot.dto.NoMessageRequest;
import com.aus.ethan.duong.chatbot.models.ChatLog;
import com.aus.ethan.duong.chatbot.services.ChatLogService;
import com.aus.ethan.duong.chatbot.services.DecisionTreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequiredArgsConstructor
public class ChatController {
    private final DecisionTreeService decisionTreeService;
    private final ChatLogService chatLogService;

    @PostMapping("/")
    public ChatResponse handleChat(@RequestBody ChatRequest chat) {
        String response = decisionTreeService.getResponse(chat.getUsername(), chat.getMessage());

        ChatLog chatLog = ChatLog
                .builder()
                .username(chat.getUsername())
                .message(chat.getMessage())
                .response(response)
                .action("USER_MESSAGE")
                .build();
        chatLogService.saveChatLog(chatLog);

        return new ChatResponse(response);
    }

    @PostMapping("/current")
    public ChatResponse getCurrentMessage(@RequestBody NoMessageRequest noMessageRequest) {
        String response = decisionTreeService.getCurrentQuestion(noMessageRequest.getUsername());

        ChatLog chatLog = ChatLog
                .builder()
                .username(noMessageRequest.getUsername())
                .response(response)
                .action("GET_CURRENT")
                .build();
        chatLogService.saveChatLog(chatLog);

        return new ChatResponse(response);
    }

    @PostMapping("/reset")
    public ChatResponse resetQuestionsForUser(@RequestBody NoMessageRequest noMessageRequest) {
        String response = decisionTreeService.reset(noMessageRequest.getUsername());

        ChatLog chatLog = ChatLog
                .builder()
                .username(noMessageRequest.getUsername())
                .response(response)
                .action("RESET")
                .build();
        chatLogService.saveChatLog(chatLog);

        return new ChatResponse(response);
    }


}
