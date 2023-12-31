package com.vinsguru.webfluxpatterns.sec01.client;

import com.vinsguru.webfluxpatterns.sec01.dto.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {

    private final WebClient client;

    public ReviewClient(@Value("${sec01.review.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    public Mono<List<Review>> getReviews(Integer id){
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()           
                /* Zip - Either all or nothing. All publisher has to emit items or nothing.
            	 * Even if one publisher emits empty signal, Mono.zip  will emit empty signal
            	 * Do not use onErrorResume which returns empty and the Zip will return empty as well
            	 * even Product info and Promotion when there is no review response
            	 * 
            	 * To more resilient, use onErrorReturn with no Promotion instance
            	 */
//                 .onErrorResume(ex->Mono.empty());
                .onErrorReturn(Collections.emptyList()); // make resilient if there is no reviews for the product when 500 or error happens
    }

}
