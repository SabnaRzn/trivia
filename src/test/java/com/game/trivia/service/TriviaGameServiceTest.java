package com.game.trivia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;

import com.game.trivia.entity.Trivia;
import com.game.trivia.modelDTO.EndQuestionResponse;
import com.game.trivia.modelDTO.TriviaQuestionDto;
import com.game.trivia.modelDTO.TriviaResponse;
import com.game.trivia.proxy.TriviaGameProxy;
import com.game.trivia.repository.TriviaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;
@SpringBootTest
public class TriviaGameServiceTest {

    @MockBean
    private TriviaGameProxy triviaGameProxy;

    @MockBean
    private TriviaRepository triviaRepository;

    @Autowired
    private TriviaGameService triviaGameService;

    @Test
    public void testStartTrivia() {
        TriviaQuestionDto questionDto = new TriviaQuestionDto();
        questionDto.setQuestion("How long are we here in UK?");
        questionDto.setCorrectAnswer("2 Years");
        questionDto.setIncorrectAnswers(Arrays.asList("3 Years", "5 Years", "6 Years"));

        TriviaResponse triviaResponse = new TriviaResponse();
        triviaResponse.setTriviaQuestionDtoList(Arrays.asList(questionDto));

        when(triviaGameProxy.fetchTrivia()).thenReturn(Mono.just(triviaResponse));

        Trivia savedTrivia = new Trivia();
        savedTrivia.setTriviaId(1L);
        savedTrivia.setQuestion("How long are we here in UK?");
        savedTrivia.setCorrectAnswer("2 Years");

        when(triviaGameProxy.fetchTrivia()).thenReturn(Mono.just(triviaResponse));
        when(triviaRepository.save(any(Trivia.class))).thenReturn(Mono.just(savedTrivia));
        Mono<EndQuestionResponse> result = triviaGameService.startTrivia();
         //Assert
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(1L, response.getTriviaId());
                    assertEquals("How long are we here in UK?", response.getQuestion());
                    assertNotNull(response.getTriviaId());
                })
                .verifyComplete();

        verify(triviaGameProxy, times(1)).fetchTrivia();
        verify(triviaRepository, times(1)).save(any(Trivia.class));

    }

}
