package com.example.pricing.controller;

import com.example.pricing.publisher.PricingSsePublisher;
import com.example.pricing.service.PlaceStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/pricing-sse")
@RequiredArgsConstructor
public class PricingSseController {

    private final PricingSsePublisher ssePublisher;
    private final PlaceStatusRepository repository;

    @GetMapping(value = "/subscribe/{quoteId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> subscribeToQuote(@PathVariable String quoteId) {
        // Perform immediate reconciliation on subscription
        repository.findByQuoteId(quoteId).collectList().subscribe(places -> {
            boolean allDraft = places.stream().allMatch(p -> p.getStatus() == PlaceStatus.DRAFT);
            boolean anyPending = places.stream().anyMatch(p -> p.getStatus() != PlaceStatus.DRAFT);

            if (allDraft) {
                ssePublisher.sendEvent(quoteId, "pricingCompletionEvent", "Pricing already complete for quote " + quoteId);
            } else if (anyPending) {
                ssePublisher.sendEvent(quoteId, "pendingPricingEvent", "Pricing pending for quote " + quoteId);
            }
        });

        return ssePublisher.subscribe(quoteId);
    }
}
