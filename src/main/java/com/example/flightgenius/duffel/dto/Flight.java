
package com.example.flightgenius.duffel.dto;

import java.util.List;

public record Flight(
        String id,
        Owner owner,
        String totalAmount,
        String totalCurrency,
        String taxCurrency,
        String taxAmount,
        List<Slice> slices,
        List<Passenger> passengers,
        boolean liveMode,
        String expiresAt,
        String createdAt,
        String baseCurrency,
        String baseAmount,
        Conditions conditions,
        List<String> availableServices,
        List<String> allowedPassengerIdentityDocumentTypes
) {
    public record Owner(String name, String id, String iataCode) {}

    public record Slice(
            List<Segment> segments,
            String originType,
            Location origin,
            String id,
            String fareBrandName,
            String duration,
            String destinationType,
            Location destination,
            Conditions conditions
    ) {}

    public record Segment(
            List<SegmentPassenger> passengers,
            String originTerminal,
            Location origin,
            String operatingCarrierFlightNumber,
            Carrier operatingCarrier,
            String marketingCarrierFlightNumber,
            Carrier marketingCarrier,
            String id,
            String duration,
            String distance,
            String destinationTerminal,
            Location destination,
            String departingAt,
            String arrivingAt,
            Aircraft aircraft
    ) {}

    public record SegmentPassenger(
            String passengerId,
            String cabinClassMarketingName,
            String cabinClass,
            List<Baggage> baggages
    ) {}

    public record Baggage(String type, int quantity) {}

    public record Location(
            String type,
            String timeZone,
            String name,
            Double longitude,
            Double latitude,
            String id,
            String icaoCode,
            String iataCountryCode,
            String iataCode,
            String iataCityCode,
            String cityName,
            City city
    ) {}

    public record City(
            String type,
            String timeZone,
            String name,
            Double longitude,
            Double latitude,
            String id,
            String icaoCode,
            String iataCountryCode,
            String iataCode,
            String iataCityCode,
            String cityName
    ) {}

    public record Passenger(String type, String id) {}

    public record Conditions(
            RefundBeforeDeparture refundBeforeDeparture,
            ChangeBeforeDeparture changeBeforeDeparture
    ) {}

    public record RefundBeforeDeparture(String penaltyCurrency, String penaltyAmount, boolean allowed) {}

    public record ChangeBeforeDeparture(String penaltyCurrency, String penaltyAmount, boolean allowed) {}

    public record Carrier(String name, String id, String iataCode) {}

    public record Aircraft(String name, String id, String iataCode) {}
}
