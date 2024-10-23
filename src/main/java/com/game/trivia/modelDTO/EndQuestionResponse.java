package com.game.trivia.modelDTO;

import lombok.Data;

import java.util.List;

@Data
public class EndQuestionResponse {
    private Long triviaId;
    private String question;

    private List<String> answers;
}
