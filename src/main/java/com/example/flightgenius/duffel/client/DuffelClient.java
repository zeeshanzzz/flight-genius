package com.example.flightgenius.duffel.client;

import com.example.flightgenius.duffel.dto.DuffelBookFlightRequest;
import com.example.flightgenius.duffel.dto.DuffelBookFlightResponse;
import com.example.flightgenius.duffel.dto.DuffelFlightSearchRequest;
import com.example.flightgenius.duffel.dto.DuffelFlightSearchResponse;
import com.example.flightgenius.duffel.dto.DuffelPayForFlightRequest;
import com.example.flightgenius.duffel.dto.DuffelPayForFlightResponse;
import com.example.flightgenius.duffel.dto.DuffelOfferDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "duffelClient", url = "${duffel.base-url}", configuration = com.example.flightgenius.duffel.config.DuffelFeignConfig.class)
public interface DuffelClient {

    @PostMapping("/air/offer_requests")
    DuffelFlightSearchResponse searchFlights(DuffelFlightSearchRequest request);

    @PostMapping("/air/orders")
    DuffelBookFlightResponse bookFlight(DuffelBookFlightRequest request);

    @PostMapping("/air/payments")
    DuffelPayForFlightResponse payForFlight(DuffelPayForFlightRequest request);

    @GetMapping("/air/offers/{offerId}")
    DuffelOfferDetailsResponse getOfferDetails(@PathVariable("offerId") String offerId);
}