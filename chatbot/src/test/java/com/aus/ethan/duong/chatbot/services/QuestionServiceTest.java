package com.aus.ethan.duong.chatbot.services;

import com.aus.ethan.duong.chatbot.models.Question;
import com.aus.ethan.duong.chatbot.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    QuestionRepository questionRepository;

    @InjectMocks
    QuestionService questionService;

    private List<Question> questionList;

    @BeforeEach
    public void setup() {
        questionList = new ArrayList<>();
    }

    @DisplayName("Test getAllQuestions")
    @Test
    public void givenChatStateUsername_whenGetChatState_thenReturnChatStateObject() {
        given(questionRepository.findAllByOrderByIdAsc())
                .willReturn(questionList);

        List<Question> allQuestions = questionService.getAllQuestions();

        assertThat(allQuestions).isNotNull();
        assertThat(allQuestions.size()).isEqualTo(0);
    }

}
