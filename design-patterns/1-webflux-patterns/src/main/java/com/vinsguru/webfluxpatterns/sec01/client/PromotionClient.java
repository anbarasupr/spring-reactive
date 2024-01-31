package com.vinsguru.webfluxpatterns.sec01.client;

import com.vinsguru.webfluxpatterns.sec01.dto.PromotionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class PromotionClient {

    private final PromotionResponse noPromotion = PromotionResponse.create(-1, "no promotion", 0.0, LocalDate.now());
    private final WebClient client;

    public PromotionClient(@Value("${sec01.promotion.service}") String baseUrl){
        this.client = WebClient.builder()
                               .baseUrl(baseUrl)
                               .build();
    }

    // Product id 5, 10, 15, 20, 25, 30, 35, 40, 45, 50 are not present in this service
    public Mono<PromotionResponse> getPromotion(Integer id){
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(PromotionResponse.class)
                /* Zip - Either all or nothing. All publisher has to emit items or nothing.
            	 * Even if one publisher emits empty signal, Mono.zip  will emit empty signal
            	 * Do not use onErrorResume which returns empty and the Zip will return empty as well
            	 * even Product info when ther is no Promotion response for that product
            	 * 
            	 * To more resilient, use onErrorReturn with no Promotion instance
            	 */
                // .onErrorResume(ex->Mono.empty());
                .onErrorReturn(noPromotion);
    }

}
