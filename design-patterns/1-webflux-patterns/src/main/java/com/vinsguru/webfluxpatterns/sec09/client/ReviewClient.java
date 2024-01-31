package com.vinsguru.webfluxpatterns.sec09.client;

import com.vinsguru.webfluxpatterns.sec09.dto.Review;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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

    public ReviewClient(@Value("${sec09.review.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    @RateLimiter(name = "review-service", fallbackMethod = "fallback")
    public Mono<List<Review>> getReviews(Integer id){
        // Promotes product sales by providing realistic Positive Reviews for the given product id. You need to pay $ for every request. 
        // Enable log by passing this property sec09.log.enabled=true to see the charges
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(Review.class)
                .collectList();
        		// .doOnNext(list->{/*put in cache*});
    }

    public Mono<List<Review>> fallback(Integer id, Throwable ex){
        // return Mono.just(Collections.emptyList());              
        List<Review> list = new ArrayList<>();
        Review defaultReview = new Review();
        defaultReview.setId(0);
        defaultReview.setUser("Default");
        defaultReview.setComment("xxx");
        list.add(defaultReview);        
        return Mono.just(list);        
        // return Mono.fromSupplier(()->{/*get from cache*/});
    }

}
