package com.vinsguru.webfluxpatterns.sec06.client;

import com.vinsguru.webfluxpatterns.sec06.dto.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {

    private final WebClient client;

    public ReviewClient(@Value("${sec06.review.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    public Mono<List<Review>> getReviews(Integer id){
    	// Provides the list of Review for the given product id. Reviews will take upto 2500 milliseconds
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .timeout(Duration.ofMillis(500)) // timeout pattern - return empty
                // .timeout(Duration.ofMillis(500), Mono.empty()) // give some fallback publisher to emit default values if timeout happens
                .onErrorReturn(Collections.emptyList());
    }

}
