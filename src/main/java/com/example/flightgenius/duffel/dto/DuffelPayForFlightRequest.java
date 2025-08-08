package com.example.flightgenius.duffel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DuffelPayForFlightRequest(
        Data data
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            String order_id,
            Payment payment
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Payment(
            String type,
            String amount,
            String currency
    ) {}
}
