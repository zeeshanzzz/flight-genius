package com.example.flightgenius.duffel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DuffelFlightSearchResponse(
    Data data
) {
    public record Data(
        List<DuffelOffer> offers,
        @JsonProperty("live_mode") boolean liveMode,
        List<Slice> slices,
        List<Passenger> passengers,
        String id
    ) {}

    public record Slice(
        String origin_type,
        Location origin,
        String id,
        String fare_brand_name,
        String duration,
        String destination_type,
        Location destination
    ) {}

    public record Location(
        String type,
        String time_zone,
        String name,
        Double longitude,
        Double latitude,
        String id,
        String icao_code,
        String iata_country_code,
        String iata_code,
        String iata_city_code,
        String city_name
    ) {}

    public record Passenger(
        String type,
        String id
    ) {}
}
