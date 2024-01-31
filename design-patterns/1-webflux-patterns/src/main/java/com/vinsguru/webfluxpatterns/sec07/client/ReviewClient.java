package com.vinsguru.webfluxpatterns.sec07.client;

import com.vinsguru.webfluxpatterns.sec07.dto.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {

    private final WebClient client;

    public ReviewClient(@Value("${sec07.review.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    public Mono<List<Review>> getReviews(Integer id){
    	// Provides the list of Review for the given product id. 70% of the requests will fail. Each requests take up to 30ms
    	
    	// product id10, 20 is not there and it throws 404. For 404, retry is not required
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())  // handle for 4xx. Retry is not required for 4xx
                .bodyToFlux(Review.class)
                .collectList()
                // .retry(5) // retry 5+1 times if any error happens
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .timeout(Duration.ofMillis(300)) // all the retry request should be responded within the timeout else return error
                .onErrorReturn(Collections.emptyList());
    }

}
