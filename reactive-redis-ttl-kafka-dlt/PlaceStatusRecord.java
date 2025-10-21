package com.example.pricing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceStatusRecord {
    private String quoteId;
    private String placeId;
    private PlaceStatus status;
    private Instant lastUpdated;
}
