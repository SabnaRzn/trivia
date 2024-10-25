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
        TriviaQuestionDto triviaQuestionDto = new TriviaQuestionDto();
        triviaQuestionDto.setQuestion("How long are we here in UK?");
        triviaQuestionDto.setCorrectAnswer("2 Years");
        triviaQuestionDto.setIncorrectAnswers(Arrays.asList("3 years", "4 years", "5 years"));

        TriviaResponse triviaResponse = new TriviaResponse();
        triviaResponse.setTriviaQuestionDtoList(List.of(triviaQuestionDto));

        Trivia savedTrivia = new Trivia();
        savedTrivia.setTriviaId(1L);
        savedTrivia.setQuestion(triviaQuestionDto.getQuestion());
        savedTrivia.setCorrectAnswer(triviaQuestionDto.getCorrectAnswer());

        given(triviaGameProxy.fetchTrivia()).willReturn(Mono.just(triviaResponse));
        given(triviaRepository.save(any(Trivia.class))).willReturn(Mono.just(savedTrivia));


        Mono<EndQuestionResponse> result = triviaGameService.startTrivia();

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTriviaId() == 1L
                        && response.getQuestion().equals("How long are we here in UK?")
                        && response.getAnswers().contains("2 Years"))
                .verifyComplete();

        then(triviaGameProxy).should(times(1)).fetchTrivia();
        then(triviaRepository).should(times(1)).save(any(Trivia.class));
    }
}
