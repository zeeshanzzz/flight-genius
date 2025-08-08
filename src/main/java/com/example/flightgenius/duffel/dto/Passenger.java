
package com.example.flightgenius.duffel.dto;

public record Passenger(
    String givenName,
    String familyName,
    String bornOn,
    String gender,
    String email,
    String phoneNumber,
    String title,
    String id,
    String infant_passenger_id
) {}
