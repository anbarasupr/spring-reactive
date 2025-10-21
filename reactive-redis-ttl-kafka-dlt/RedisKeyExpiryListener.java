package com.example.pricing.service;

import com.example.pricing.dto.PlaceExpiredMessage;
import com.example.pricing.model.PlaceStatusRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.repository.events.RedisKeyExpiredEvent;
import org.springframework.kafka.core.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisKeyExpiryListener implements ApplicationListener<RedisKeyExpiredEvent<PlaceStatusRecord>> {

    private final ReactiveKafkaProducerTemplate<String, PlaceExpiredMessage> kafkaProducer;

    @Override
    public void onApplicationEvent(RedisKeyExpiredEvent<PlaceStatusRecord> event) {
        PlaceStatusRecord expiredRecord = event.getValue();
        if (expiredRecord != null) {
            PlaceExpiredMessage msg = new PlaceExpiredMessage(expiredRecord.getQuoteId(), expiredRecord.getPlaceId());
            kafkaProducer.send("pricing-dead-letter-topic", msg.getQuoteId(), msg).subscribe();
        }
    }
}
