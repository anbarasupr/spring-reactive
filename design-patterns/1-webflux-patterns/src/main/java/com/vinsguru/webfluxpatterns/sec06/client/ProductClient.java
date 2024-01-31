package com.vinsguru.webfluxpatterns.sec06.client;

import com.vinsguru.webfluxpatterns.sec06.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ProductClient {

    private final WebClient client;

    public ProductClient(@Value("${sec06.product.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    public Mono<Product> getProduct(Integer id){
    	// Provides the product details for the given product id
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .timeout(Duration.ofMillis(500))	// timeout pattern - return empty
                // .timeout(Duration.ofMillis(500), Mono.empty()) // give some fallback publisher to emit default values if timeout happens
                .onErrorResume(ex -> Mono.empty());
    }

}
