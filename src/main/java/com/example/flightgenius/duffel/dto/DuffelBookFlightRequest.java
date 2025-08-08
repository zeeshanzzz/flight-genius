package com.example.flightgenius.duffel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DuffelBookFlightRequest(
        Data data
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Data(
            List<String> selected_offers,
            List<Payment> payments,
            List<Passenger> passengers
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Payment(
            String type,
            String currency,
            String amount
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Passenger(
            String id,
            String phone_number,
            String email,
            String born_on,
            String title,
            String gender,
            String family_name,
            String given_name,
            String infant_passenger_id
    ) {}
}
