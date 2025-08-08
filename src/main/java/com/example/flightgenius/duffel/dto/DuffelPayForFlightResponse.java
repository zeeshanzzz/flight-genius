package com.example.flightgenius.duffel.dto;

public record DuffelPayForFlightResponse(
    Data data
) {
    public record Data(
        String id,
        String status
        // Add more fields as needed
    ) {}
}
