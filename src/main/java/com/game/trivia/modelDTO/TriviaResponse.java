package com.game.trivia.modelDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TriviaResponse {
    @JsonProperty("response_code")
    private int responseCode;
    @JsonProperty("results")
    private List<TriviaQuestionDto> triviaQuestionDtoList;

    public TriviaResponse() {
        this.responseCode = responseCode;
        this.triviaQuestionDtoList = triviaQuestionDtoList;
    }

}
