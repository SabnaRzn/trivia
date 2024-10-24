package com.game.trivia.controller;

import com.game.trivia.modelDTO.EndQuestionResponse;
import com.game.trivia.modelDTO.TriviaAnswerRequest;
import com.game.trivia.service.TriviaGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/trivia")
@RequiredArgsConstructor
public class TriviaGameController {

    @Autowired
    private final TriviaGameService triviaGameService;

    @PostMapping("/start")
    public Mono<ResponseEntity<EndQuestionResponse>> triviaStart() {
        return triviaGameService.startTrivia()
                .map(response -> ResponseEntity.ok(response))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

        @PutMapping("/reply/{id}")
    public Mono<ResponseEntity<Map<String, String>>> replyToTrivia(@PathVariable Long id, @RequestBody TriviaAnswerRequest request) {
        String userAnswer = request.getAnswer();
                return triviaGameService.replyToTrivia(id, userAnswer)
                        .map(result -> ResponseEntity.ok(Collections.singletonMap("result", result)))
                        .onErrorResume(NoSuchElementException.class, e ->
                                Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("result", "No such question!"))))
                        .onErrorResume(e ->
                                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("result", "An error occurred!"))));


        }

}
