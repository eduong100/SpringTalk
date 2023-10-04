package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.Question;
import com.aus.ethan.duong.chatbot.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public List<Question> getAllQuestions() {
        return questionRepository.findAllByOrderByIdAsc();
    }
}
