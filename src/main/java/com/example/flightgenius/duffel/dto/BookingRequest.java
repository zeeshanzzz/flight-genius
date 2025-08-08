
package com.example.flightgenius.duffel.dto;

import java.util.List;

public record BookingRequest(
    String flightId,
    List<Passenger> passengers,
    String amount,
    String currency
) {}
