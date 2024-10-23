package com.game.trivia.service;

import com.game.trivia.entity.Trivia;
import com.game.trivia.modelDTO.EndQuestionResponse;
import com.game.trivia.modelDTO.TriviaAnswerResponse;
import com.game.trivia.modelDTO.TriviaQuestionDto;
import com.game.trivia.proxy.TriviaGameProxy;
import com.game.trivia.repository.TriviaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class TriviaGameService {

    private final TriviaGameProxy triviaGameProxy;
    private final TriviaRepository triviaRepository;

    Logger logger = LoggerFactory.getLogger(TriviaGameService.class);


    @Autowired
    public TriviaGameService(TriviaGameProxy triviaGameProxy, TriviaRepository triviaRepository) {
        this.triviaGameProxy = triviaGameProxy;
        this.triviaRepository = triviaRepository;
    }

    public Mono<EndQuestionResponse> startTrivia() {
        return triviaGameProxy.fetchTrivia()
                .flatMap(response -> {
                    TriviaQuestionDto questionDto = response.getTriviaQuestionDtoList().get(0);
                    Trivia trivia = new Trivia();
                    trivia.setQuestion(questionDto.getQuestion());
                    trivia.setCorrectAnswer(questionDto.getCorrectAnswer());
                    trivia.setAnswerAttempts(0);

                    // Save the trivia question in the database within the reactive chain
                    return triviaRepository.save(trivia)  // Return the saved trivia Mono
                            .flatMap(savedTrivia -> {
                                EndQuestionResponse endQuestionResponse = new EndQuestionResponse();
                                endQuestionResponse.setTriviaId(savedTrivia.getTriviaId());
                                endQuestionResponse.setQuestion(savedTrivia.getQuestion());

                                List<String> allAnswers = new ArrayList<>(questionDto.getIncorrectAnswers());
                                allAnswers.add(savedTrivia.getCorrectAnswer());
                                Collections.shuffle(allAnswers);

                                endQuestionResponse.setAnswers(allAnswers);
                                return Mono.just(endQuestionResponse);
                            });

                });
    }


    public Mono<String> replyToTrivia(Long id, String answer) {
        return triviaRepository.findById(id)
                .flatMap(trivia -> {
                    if (trivia.getAnswerAttempts() >= 3) {
                        return Mono.just("Max attempts reached! Right Answer:"+trivia.getCorrectAnswer());
                    }

                    if (trivia.getCorrectAnswer().equalsIgnoreCase(answer)) {
                        return triviaRepository.delete(trivia).then(Mono.just("right!"));
                    } else {
                        trivia.setAnswerAttempts(trivia.getAnswerAttempts() + 1);
                        return triviaRepository.save(trivia)
                                .then(Mono.just(trivia.getAnswerAttempts() >= 3 ? "Max attempts reached! Right Answer:"+trivia.getCorrectAnswer() : "wrong!"));
                    }
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("No such question!")));
    }

}

