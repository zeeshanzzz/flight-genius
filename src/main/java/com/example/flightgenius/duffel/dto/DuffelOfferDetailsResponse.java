package com.example.flightgenius.duffel.dto;

import java.util.List;

public record DuffelOfferDetailsResponse(
    Data data
) {
    public record Data(
        String updated_at,
        String total_emissions_kg,
        String total_currency,
        String total_amount,
        String tax_currency,
        String tax_amount,
        List<Slice> slices,
        PaymentRequirements payment_requirements,
        List<Passenger> passengers,
        boolean passenger_identity_documents_required,
        Owner owner,
        boolean live_mode,
        String id,
        String expires_at,
        String created_at,
        Conditions conditions,
        String base_currency,
        String base_amount,
        List<Object> available_services,
        List<Object> allowed_passenger_identity_document_types
    ) {}
    public record Slice(
        List<Segment> segments,
        String origin_type,
        Location origin,
        String id,
        String fare_brand_name,
        String duration,
        String destination_type,
        Location destination,
        Conditions conditions
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
        Aircraft aircraft
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
        City city,
        List<Object> airports
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
    public record PaymentRequirements(
        boolean requires_instant_payment,
        String price_guarantee_expires_at,
        String payment_required_by
    ) {}
    public record Passenger(
        String type,
        String id
    ) {}
    public record Owner(
        String name,
        String id,
        String iata_code
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
}
