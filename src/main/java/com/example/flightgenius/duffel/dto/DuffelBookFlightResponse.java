package com.example.flightgenius.duffel.dto;

import java.util.List;

public record DuffelBookFlightResponse(
    Data data
) {
    public record Data(
        String id,
        String booking_reference,
        String total_currency,
        String total_amount,
        String tax_currency,
        String tax_amount,
        List<Slice> slices,
        List<Passenger> passengers,
        Owner owner,
        boolean live_mode,
        List<Document> documents,
        String created_at,
        String cancelled_at,
        String base_currency,
        String base_amount,
        PaymentStatus payment_status,
        Conditions conditions
    ) {}

    public record Slice(
        List<Segment> segments,
        String origin_type,
        Location origin,
        String id,
        String duration,
        String destination_type,
        Location destination,
        Conditions conditions,
        Boolean changeable
    ) {}

    public record Segment(
        List<SegmentPassenger> passengers,
        String origin_terminal,
        Location origin,
        String operating_carrier_flight_number,
        Carrier operating_carrier,
        String marketing_carrier_flight_number,
        Carrier marketing_carrier,
        String id,
        String duration,
        String distance,
        String destination_terminal,
        Location destination,
        String departing_at,
        String arriving_at,
        Aircraft aircraft,
        String departure_terminal,
        String departure_datetime,
        String arrival_terminal,
        String arrival_datetime
    ) {}

    public record SegmentPassenger(
        String passenger_id,
        String cabin_class_marketing_name,
        String cabin_class,
        List<Baggage> baggages
    ) {}

    public record Baggage(
        String type,
        int quantity
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
        String city_name,
        City city
    ) {}

    public record City(
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
        String title,
        String infant_passenger_id,
        String id,
        String given_name,
        String gender,
        String family_name,
        String born_on
    ) {}

    public record Owner(
        String name,
        String id,
        String iata_code
    ) {}

    public record Document(
        String unique_identifier,
        String type
    ) {}

    public record PaymentStatus(
        String price_guarantee_expires_at,
        String payment_required_by,
        boolean awaiting_payment
    ) {}

    public record Conditions(
        RefundBeforeDeparture refund_before_departure,
        ChangeBeforeDeparture change_before_departure
    ) {}

    public record RefundBeforeDeparture(
        String penalty_currency,
        String penalty_amount,
        boolean allowed
    ) {}

    public record ChangeBeforeDeparture(
        String penalty_currency,
        String penalty_amount,
        boolean allowed
    ) {}

    public record Carrier(
        String name,
        String id,
        String iata_code
    ) {}

    public record Aircraft(
        String name,
        String id,
        String iata_code
    ) {}
}
