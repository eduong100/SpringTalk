package com.aus.ethan.duong.chatbot.repositories;

import com.aus.ethan.duong.chatbot.models.ChatState;
import com.aus.ethan.duong.chatbot.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findAllByOrderByIdAsc();

}
