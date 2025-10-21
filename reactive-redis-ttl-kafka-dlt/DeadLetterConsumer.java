package com.example.pricing.service;

import com.example.pricing.dto.PlaceExpiredMessage;
import com.example.pricing.model.PlaceStatus;
import com.example.pricing.model.PlaceStatusRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeadLetterConsumer {

    private final PlaceStatusRepository repository;
    private final PricingSsePublisher ssePublisher;
    private static final Duration GRACE_PERIOD = Duration.ofSeconds(5);

    @KafkaListener(topics = "pricing-dead-letter-topic", groupId = "pricing-expiry-group")
    public void processExpiredPlace(PlaceExpiredMessage message) {
        Mono.delay(GRACE_PERIOD)
            .thenMany(repository.findByQuoteId(message.getQuoteId()).collectList())
            .subscribe(places -> {
                boolean allDraft = places.stream().allMatch(p -> p.getStatus() == PlaceStatus.DRAFT);
                boolean anyPending = places.stream().anyMatch(p -> p.getStatus() != PlaceStatus.DRAFT);

                if (allDraft) {
                    ssePublisher.sendEventIfRelevant(message.getQuoteId(), "pricingCompletionEvent",
                        "Pricing completed for quote " + message.getQuoteId(), repository);
                } else if (anyPending) {
                    ssePublisher.sendEventIfRelevant(message.getQuoteId(), "pendingPricingEvent",
                        "Pricing pending for quote " + message.getQuoteId(), repository);
                }
            });
    }
}
