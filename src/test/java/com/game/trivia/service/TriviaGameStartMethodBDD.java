package com.game.trivia.service;
import com.game.trivia.entity.Trivia;
import com.game.trivia.modelDTO.EndQuestionResponse;
import com.game.trivia.modelDTO.TriviaQuestionDto;
import com.game.trivia.modelDTO.TriviaResponse;
import com.game.trivia.proxy.TriviaGameProxy;
import com.game.trivia.repository.TriviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class TriviaGameStartMethodBDD {

    @Mock
    private TriviaGameProxy triviaGameProxy;

    @Mock
    private TriviaRepository triviaRepository;

    @InjectMocks
    private TriviaGameService triviaGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenTriviaFetched_WhenStartTrivia_ThenReturnEndQuestionResponse() {
        // Given
        TriviaQuestionDto triviaQuestionDto = new TriviaQuestionDto();
        triviaQuestionDto.setQuestion("What is the capital of France?");
        triviaQuestionDto.setCorrectAnswer("Paris");
        triviaQuestionDto.setIncorrectAnswers(Arrays.asList("Berlin", "Rome", "London"));

        TriviaResponse triviaResponse = new TriviaResponse();
        triviaResponse.setTriviaQuestionDtoList(List.of(triviaQuestionDto));

        Trivia savedTrivia = new Trivia();
        savedTrivia.setTriviaId(1L);
        savedTrivia.setQuestion(triviaQuestionDto.getQuestion());
        savedTrivia.setCorrectAnswer(triviaQuestionDto.getCorrectAnswer());

        given(triviaGameProxy.fetchTrivia()).willReturn(Mono.just(triviaResponse));
        given(triviaRepository.save(any(Trivia.class))).willReturn(Mono.just(savedTrivia));

        // When
        Mono<EndQuestionResponse> result = triviaGameService.startTrivia();

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTriviaId() == 1L
                        && response.getQuestion().equals("What is the capital of France?")
                        && response.getAnswers().contains("Paris"))
                .verifyComplete();

        then(triviaGameProxy).should(times(1)).fetchTrivia();
        then(triviaRepository).should(times(1)).save(any(Trivia.class));
    }
}
