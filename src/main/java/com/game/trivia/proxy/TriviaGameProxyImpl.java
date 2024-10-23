package com.game.trivia.proxy;

import com.game.trivia.modelDTO.TriviaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TriviaGameProxyImpl implements TriviaGameProxy {

    private final WebClient webClient;

    @Autowired
    public TriviaGameProxyImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://opentdb.com").build();
    }

    @Override
    public Mono<TriviaResponse> fetchTrivia() {
        return this.webClient.get()
                .uri("/api.php?amount=1")
                .retrieve()
                .bodyToMono(TriviaResponse.class);
    }


}

