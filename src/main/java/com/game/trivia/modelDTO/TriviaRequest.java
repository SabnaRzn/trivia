package com.game.trivia.modelDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TriviaRequest {
    @JsonProperty("correctAnswer")
    private String correctAnswer;
}