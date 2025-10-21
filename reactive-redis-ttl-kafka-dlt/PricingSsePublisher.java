package com.example.pricing.publisher;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PricingSsePublisher {

    private final Map<String, Sinks.Many<ServerSentEvent<String>>> quoteSinks = new ConcurrentHashMap<>();

    public Flux<ServerSentEvent<String>> subscribe(String quoteId) {
        Sinks.Many<ServerSentEvent<String>> sink = Sinks.many().multicast().directBestEffort();
        quoteSinks.put(quoteId, sink);
        return sink.asFlux()
                   .doFinally(signal -> quoteSinks.remove(quoteId));
    }

    public void sendEvent(String quoteId, String event, String data) {
        Optional.ofNullable(quoteSinks.get(quoteId))
                .ifPresent(sink -> sink.tryEmitNext(ServerSentEvent.builder().event(event).data(data).build()));
    }

    public void sendEventIfRelevant(String quoteId, String event, String data, PlaceStatusRepository repository) {
        repository.findByQuoteId(quoteId).collectList().subscribe(places -> {
            boolean allDraft = places.stream().allMatch(p -> p.getStatus() == PlaceStatus.DRAFT);
            boolean anyPending = places.stream().anyMatch(p -> p.getStatus() != PlaceStatus.DRAFT);

            if (allDraft && event.equals("pricingCompletionEvent")) {
                sendEvent(quoteId, event, data);
            } else if (anyPending && event.equals("pendingPricingEvent")) {
                sendEvent(quoteId, event, data);
            }
        });
    }
}
