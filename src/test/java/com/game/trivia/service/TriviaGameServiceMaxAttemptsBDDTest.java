package com.game.trivia.service;
import com.game.trivia.entity.Trivia;
import com.game.trivia.repository.TriviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

public class TriviaGameServiceMaxAttemptsBDDTest {

    @Mock
    private TriviaRepository triviaRepository;

    @InjectMocks
    private TriviaGameService triviaGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenMaxAttemptsReached_WhenReplyToTrivia_ThenReturnMaxAttemptsMessageAndDeleteTrivia() {
        // Given
        Long triviaId = 1L;
        Trivia trivia = new Trivia();
        trivia.setTriviaId(triviaId);
        trivia.setQuestion("What is the capital of France?");
        trivia.setCorrectAnswer("Paris");
        trivia.setAnswerAttempts(3);

        given(triviaRepository.findById(triviaId)).willReturn(Mono.just(trivia));
        given(triviaRepository.save(trivia)).willReturn(Mono.just(trivia));
        given(triviaRepository.delete(trivia)).willReturn(Mono.empty());

        // When
        Mono<String> result = triviaGameService.replyToTrivia(triviaId, "Berlin");

        // Then
        StepVerifier.create(result)
                .expectNext("Max attempts reached! Right Answer:Paris")
                .verifyComplete();

        then(triviaRepository).should(times(1)).findById(triviaId);
        then(triviaRepository).should(times(1)).save(trivia);
        then(triviaRepository).should(times(1)).delete(trivia);
    }


}
