
package com.example.flightgenius.duffel.dto;

import java.util.List;

public record SearchResponse(
    List<Flight> flights
) {}
