package com.game.trivia.proxy;

import com.game.trivia.modelDTO.TriviaResponse;
import reactor.core.publisher.Mono;

public interface TriviaGameProxy {
    Mono<TriviaResponse> fetchTrivia();
}
