package com.game.trivia.repository;

import com.game.trivia.entity.Trivia;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TriviaRepository extends ReactiveCrudRepository<Trivia, Long> {
}

