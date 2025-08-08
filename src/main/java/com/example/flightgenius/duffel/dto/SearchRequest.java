
package com.example.flightgenius.duffel.dto;

public record SearchRequest(
    String origin,
    String destination,
    String departureDate,
    String returnDate,
    int adults,
    int children,
    int infants,
    String cabinClass,
    int maxConnections
) {}
