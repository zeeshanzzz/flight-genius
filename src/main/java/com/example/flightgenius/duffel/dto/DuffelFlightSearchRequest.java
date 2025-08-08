package com.example.flightgenius.duffel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DuffelFlightSearchRequest(
        Data data
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            List<Slice> slices,
            List<Passenger> passengers,
            String cabin_class
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Slice(
            String origin,
            String destination,
            String departure_date
    ) {}

    public record Passenger(
            String type,
            Integer age
    ) {
        public Passenger {
            if (type == null || type.isBlank()) type = "adult";
        }
    }
}
