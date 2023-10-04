package com.aus.ethan.duong.chatbot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="chatState")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatState {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String state;
}
