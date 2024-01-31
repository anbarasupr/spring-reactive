package com.vinsguru.webfluxpatterns.sec08.client;

import com.vinsguru.webfluxpatterns.sec08.dto.Review;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {

    private final WebClient client;

    public ReviewClient(@Value("${sec08.review.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    @CircuitBreaker(name = "review-service", fallbackMethod = "fallBackReview")    
    public Mono<List<Review>> getReviews(Integer id){
    	// Provides the list of Review for the given product id. Service will fail periodically. (maintenance window every other 30 seconds)
        // Enable log by passing this property sec08.log.enabled=true
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(Review.class)
                .collectList()
                .retry(5)
                .timeout(Duration.ofMillis(300)); 
        		// All the retry is not able to complete within this time, it will time out exception. so we have to record Timeoutexception in
        		// recordExceptions for circuit breaker else it will ignore.
        
        
        		// .onErrorReturn(Collections.emptyList()); 
        		// Comment above for circuit breaker. For the 500 errors, it is returning default value and it wont considered as exception for the Circuit breaker.
        		// Circuit Breaker expects the expections to be thrown , only then it will decisions to go to fallbackMethod

    }

    public Mono<List<Review>> fallBackReview(Integer id, Throwable ex){
        System.out.println("fallback reviews called : " + ex.getMessage());
        // return Mono.just(Collections.emptyList());
        List<Review> list = new ArrayList<>();
        Review defaultReview = new Review();
        defaultReview.setId(0);
        defaultReview.setUser("Default");
        defaultReview.setComment("xxx");
        list.add(defaultReview);        
        return Mono.just(list);
    }

}
