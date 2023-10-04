package com.aus.ethan.duong.chatbot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="chatLogs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String message;

    @CreationTimestamp
    private Timestamp timestamp;

    private String response;

    private String action;
}
