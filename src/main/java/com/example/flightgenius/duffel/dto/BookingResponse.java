
package com.example.flightgenius.duffel.dto;

public record BookingResponse(
    String bookingId,
    String status,
    double totalAmount,
    String currency
) {}
