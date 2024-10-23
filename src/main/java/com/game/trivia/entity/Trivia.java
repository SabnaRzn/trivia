package com.game.trivia.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Data
@Table("trivia")
public class Trivia {
    @Id
    @Column("trivia_id")
    private Long triviaId;

    private String question;

    private String correctAnswer;
    private int answerAttempts;

}
