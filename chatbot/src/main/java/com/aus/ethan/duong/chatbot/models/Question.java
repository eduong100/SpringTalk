package com.aus.ethan.duong.chatbot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name="questions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String parent;

    private String previous_decision;

    @Column(nullable = false)
    private String text;
}
