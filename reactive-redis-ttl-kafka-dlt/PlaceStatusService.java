package com.example.pricing.service;

import com.example.pricing.model.PlaceStatus;
import com.example.pricing.model.PlaceStatusRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PlaceStatusService {

    private final ReactiveRedisTemplate<String, PlaceStatusRecord> redis;

    public Mono<Void> setInitialStatus(String quoteId, String placeId, Duration ttl) {
        String key = "place:" + quoteId + ":" + placeId;
        PlaceStatusRecord record = new PlaceStatusRecord(quoteId, placeId, PlaceStatus.INITIAL, Instant.now());
        return redis.opsForValue().set(key, record, ttl);
    }

    public Mono<Void> updatePendingDraftStatus(String quoteId, String placeId) {
        String key = "place:" + quoteId + ":" + placeId;
        PlaceStatusRecord record = new PlaceStatusRecord(quoteId, placeId, PlaceStatus.PENDING_DRAFT, Instant.now());
        return redis.opsForValue().set(key, record);
    }

    public Mono<Void> updateDraftStatusAndCancelExpiry(String quoteId, String placeId) {
        String key = "place:" + quoteId + ":" + placeId;
        PlaceStatusRecord record = new PlaceStatusRecord(quoteId, placeId, PlaceStatus.DRAFT, Instant.now());
        // Atomic update: set without expiration and update value
        return redis.opsForValue().getOperations().execute(connection -> {
            byte[] rawKey = redis.getStringSerializer().serialize(key);
            byte[] rawValue = redis.getValueSerializer().serialize(record);
            connection.multi();
            connection.set(rawKey, rawValue);
            connection.persist(rawKey); // remove TTL
            return connection.exec();
        }).then();
    }
}
