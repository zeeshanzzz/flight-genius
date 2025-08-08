
package com.example.flightgenius.duffel.dto;

public record PaymentRequest(
    String bookingId,
    String paymentToken,
    String amount,
    String currency
) {}
