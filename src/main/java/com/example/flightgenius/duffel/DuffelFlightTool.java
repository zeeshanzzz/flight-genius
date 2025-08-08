package com.example.flightgenius.duffel;

import com.example.flightgenius.duffel.client.DuffelClient;
import com.example.flightgenius.duffel.dto.BookingRequest;
import com.example.flightgenius.duffel.dto.BookingResponse;
import com.example.flightgenius.duffel.dto.PaymentRequest;
import com.example.flightgenius.duffel.dto.PaymentResponse;
import com.example.flightgenius.duffel.dto.SearchRequest;
import com.example.flightgenius.duffel.dto.SearchResponse;
import com.example.flightgenius.duffel.dto.Flight;
import com.example.flightgenius.duffel.dto.DuffelBookFlightRequest;
import com.example.flightgenius.duffel.dto.DuffelBookFlightResponse;
import com.example.flightgenius.duffel.dto.DuffelFlightSearchRequest;
import com.example.flightgenius.duffel.dto.DuffelFlightSearchResponse;
import com.example.flightgenius.duffel.dto.DuffelPayForFlightRequest;
import com.example.flightgenius.duffel.dto.DuffelPayForFlightResponse;
import com.example.flightgenius.duffel.dto.DuffelOfferDetailsResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DuffelFlightTool {
    private final DuffelClient duffelClient;

    public DuffelFlightTool(DuffelClient duffelClient) {
        this.duffelClient = duffelClient;
    }

    @Value("${duffel.api-key}")
    private String apiKey;
    private static final String DUFFEL_VERSION = "v2";
    private static final String CONTENT_TYPE = "application/json";

    @Tool(name = "selectOffer", description = "Get full details for a selected offer using Duffel API. Requires offerId.")
    public DuffelOfferDetailsResponse selectOffer(String offerId) {
        log.info("Selecting offer: offerId={}", offerId);
        DuffelOfferDetailsResponse response = duffelClient.getOfferDetails(offerId);
        log.debug("Duffel offer details response: {}", response);
        return response;
    }

    @Tool(name = "searchFlights", description = "Search for available flights using Duffel API. Requires origin, destination, departure date, and cabin class.")
    public SearchResponse searchFlights(SearchRequest request) {
        log.info("Searching flights: origin={}, destination={}, departureDate={}, returnDate={}, adults={}, children={}, infants={}, cabinClass={}, maxConnections={}",
                request.origin(), request.destination(), request.departureDate(), request.returnDate(), request.adults()
                , request.children(), request.infants(), request.cabinClass(), request.maxConnections());

        // Always build slices with both outbound and return if provided
        var slices = new java.util.ArrayList<DuffelFlightSearchRequest.Slice>();
        slices.add(new DuffelFlightSearchRequest.Slice(
                request.origin(),
                request.destination(),
                request.departureDate()
        ));
        if (request.returnDate() != null && !request.returnDate().isBlank()) {
            slices.add(new DuffelFlightSearchRequest.Slice(
                    request.destination(),
                    request.origin(),
                    request.returnDate()
            ));
        }

        // Add all passenger types with proper values (age only for infants)
        var passengers = new java.util.ArrayList<DuffelFlightSearchRequest.Passenger>();
        for (int i = 0; i < request.adults(); i++) {
            passengers.add(new DuffelFlightSearchRequest.Passenger("adult", null));
        }
        for (int i = 0; i < request.children(); i++) {
            passengers.add(new DuffelFlightSearchRequest.Passenger("child", null));
        }
        for (int i = 0; i < request.infants(); i++) {
            passengers.add(new DuffelFlightSearchRequest.Passenger("infant", 1)); // age 1 for infant
        }
        if (passengers.isEmpty()) {
            passengers.add(new DuffelFlightSearchRequest.Passenger("adult", null)); // fallback
        }

        var duffelRequest = new DuffelFlightSearchRequest(
                new DuffelFlightSearchRequest.Data(
                        slices,
                        passengers,
                        request.cabinClass() != null ? request.cabinClass() : "economy"
                )
        );
        log.info("Duffel searchFlights request payload: {}", duffelRequest);
        DuffelFlightSearchResponse duffelResponse = duffelClient.searchFlights(duffelRequest);
        log.debug("Duffel search response: {}", duffelResponse);
        List<Flight> flights = duffelResponse.data().offers().stream().map(offer ->
                new Flight(
                        offer.id(),
                        offer.owner() != null ? new Flight.Owner(
                                offer.owner().name(),
                                offer.owner().id(),
                                offer.owner().iata_code()
                        ) : null,
                        offer.total_amount(),
                        offer.total_currency(),
                        offer.tax_currency(),
                        offer.tax_amount(),
                        offer.slices() != null ? offer.slices().stream().map(slice ->
                                new Flight.Slice(
                                        slice.segments() != null ? slice.segments().stream().map(segment ->
                                                new Flight.Segment(
                                                        segment.passengers() != null ? segment.passengers().stream().map(sp ->
                                                                new Flight.SegmentPassenger(
                                                                        sp.passenger_id(),
                                                                        sp.cabin_class_marketing_name(),
                                                                        sp.cabin_class(),
                                                                        sp.baggages() != null ? sp.baggages().stream().map(b ->
                                                                                new Flight.Baggage(b.type(), b.quantity())
                                                                        ).toList() : List.of()
                                                                )
                                                        ).toList() : List.of(),
                                                        segment.origin_terminal(),
                                                        segment.origin() != null ? new Flight.Location(
                                                                segment.origin().type(),
                                                                segment.origin().time_zone(),
                                                                segment.origin().name(),
                                                                segment.origin().longitude(),
                                                                segment.origin().latitude(),
                                                                segment.origin().id(),
                                                                segment.origin().icao_code(),
                                                                segment.origin().iata_country_code(),
                                                                segment.origin().iata_code(),
                                                                segment.origin().iata_city_code(),
                                                                segment.origin().city_name(),
                                                                segment.origin().city() != null ? new Flight.City(
                                                                        segment.origin().city().type(),
                                                                        segment.origin().city().time_zone(),
                                                                        segment.origin().city().name(),
                                                                        segment.origin().city().longitude(),
                                                                        segment.origin().city().latitude(),
                                                                        segment.origin().city().id(),
                                                                        segment.origin().city().icao_code(),
                                                                        segment.origin().city().iata_country_code(),
                                                                        segment.origin().city().iata_code(),
                                                                        segment.origin().city().iata_city_code(),
                                                                        segment.origin().city().city_name()
                                                                ) : null
                                                        ) : null,
                                                        segment.operating_carrier_flight_number(),
                                                        segment.operating_carrier() != null ? new Flight.Carrier(
                                                                segment.operating_carrier().name(),
                                                                segment.operating_carrier().id(),
                                                                segment.operating_carrier().iata_code()
                                                        ) : null,
                                                        segment.marketing_carrier_flight_number(),
                                                        segment.marketing_carrier() != null ? new Flight.Carrier(
                                                                segment.marketing_carrier().name(),
                                                                segment.marketing_carrier().id(),
                                                                segment.marketing_carrier().iata_code()
                                                        ) : null,
                                                        segment.id(),
                                                        segment.duration(),
                                                        segment.distance(),
                                                        segment.destination_terminal(),
                                                        segment.destination() != null ? new Flight.Location(
                                                                segment.destination().type(),
                                                                segment.destination().time_zone(),
                                                                segment.destination().name(),
                                                                segment.destination().longitude(),
                                                                segment.destination().latitude(),
                                                                segment.destination().id(),
                                                                segment.destination().icao_code(),
                                                                segment.destination().iata_country_code(),
                                                                segment.destination().iata_code(),
                                                                segment.destination().iata_city_code(),
                                                                segment.destination().city_name(),
                                                                segment.destination().city() != null ? new Flight.City(
                                                                        segment.destination().city().type(),
                                                                        segment.destination().city().time_zone(),
                                                                        segment.destination().city().name(),
                                                                        segment.destination().city().longitude(),
                                                                        segment.destination().city().latitude(),
                                                                        segment.destination().city().id(),
                                                                        segment.destination().city().icao_code(),
                                                                        segment.destination().city().iata_country_code(),
                                                                        segment.destination().city().iata_code(),
                                                                        segment.destination().city().iata_city_code(),
                                                                        segment.destination().city().city_name()
                                                                ) : null
                                                        ) : null,
                                                        segment.departing_at(),
                                                        segment.arriving_at(),
                                                        segment.aircraft() != null ? new Flight.Aircraft(
                                                                segment.aircraft().name(),
                                                                segment.aircraft().id(),
                                                                segment.aircraft().iata_code()
                                                        ) : null
                                                )
                                        ).toList() : List.of(),
                                        slice.origin_type(),
                                        slice.origin() != null ? new Flight.Location(
                                                slice.origin().type(),
                                                slice.origin().time_zone(),
                                                slice.origin().name(),
                                                slice.origin().longitude(),
                                                slice.origin().latitude(),
                                                slice.origin().id(),
                                                slice.origin().icao_code(),
                                                slice.origin().iata_country_code(),
                                                slice.origin().iata_code(),
                                                slice.origin().iata_city_code(),
                                                slice.origin().city_name(),
                                                slice.origin().city() != null ? new Flight.City(
                                                        slice.origin().city().type(),
                                                        slice.origin().city().time_zone(),
                                                        slice.origin().city().name(),
                                                        slice.origin().city().longitude(),
                                                        slice.origin().city().latitude(),
                                                        slice.origin().city().id(),
                                                        slice.origin().city().icao_code(),
                                                        slice.origin().city().iata_country_code(),
                                                        slice.origin().city().iata_code(),
                                                        slice.origin().city().iata_city_code(),
                                                        slice.origin().city().city_name()
                                                ) : null
                                        ) : null,
                                        slice.id(),
                                        slice.fare_brand_name(),
                                        slice.duration(),
                                        slice.destination_type(),
                                        slice.destination() != null ? new Flight.Location(
                                                slice.destination().type(),
                                                slice.destination().time_zone(),
                                                slice.destination().name(),
                                                slice.destination().longitude(),
                                                slice.destination().latitude(),
                                                slice.destination().id(),
                                                slice.destination().icao_code(),
                                                slice.destination().iata_country_code(),
                                                slice.destination().iata_code(),
                                                slice.destination().iata_city_code(),
                                                slice.destination().city_name(),
                                                slice.destination().city() != null ? new Flight.City(
                                                        slice.destination().city().type(),
                                                        slice.destination().city().time_zone(),
                                                        slice.destination().city().name(),
                                                        slice.destination().city().longitude(),
                                                        slice.destination().city().latitude(),
                                                        slice.destination().city().id(),
                                                        slice.destination().city().icao_code(),
                                                        slice.destination().city().iata_country_code(),
                                                        slice.destination().city().iata_code(),
                                                        slice.destination().city().iata_city_code(),
                                                        slice.destination().city().city_name()
                                                ) : null
                                        ) : null,
                                        slice.conditions() != null ? new Flight.Conditions(
                                                slice.conditions().refund_before_departure() != null ? new Flight.RefundBeforeDeparture(
                                                        slice.conditions().refund_before_departure().penalty_currency(),
                                                        slice.conditions().refund_before_departure().penalty_amount(),
                                                        slice.conditions().refund_before_departure().allowed()
                                                ) : null,
                                                slice.conditions().change_before_departure() != null ? new Flight.ChangeBeforeDeparture(
                                                        slice.conditions().change_before_departure().penalty_currency(),
                                                        slice.conditions().change_before_departure().penalty_amount(),
                                                        slice.conditions().change_before_departure().allowed()
                                                ) : null
                                        ) : null
                                )
                        ).toList() : List.of(),
                        offer.passengers() != null ? offer.passengers().stream().map(p ->
                                new Flight.Passenger(p.type(), p.id())
                        ).toList() : List.of(),
                        offer.live_mode(),
                        offer.expires_at(),
                        offer.created_at(),
                        offer.base_currency(),
                        offer.base_amount(),
                        offer.conditions() != null ? new Flight.Conditions(
                                offer.conditions().refund_before_departure() != null ? new Flight.RefundBeforeDeparture(
                                        offer.conditions().refund_before_departure().penalty_currency(),
                                        offer.conditions().refund_before_departure().penalty_amount(),
                                        offer.conditions().refund_before_departure().allowed()
                                ) : null,
                                offer.conditions().change_before_departure() != null ? new Flight.ChangeBeforeDeparture(
                                        offer.conditions().change_before_departure().penalty_currency(),
                                        offer.conditions().change_before_departure().penalty_amount(),
                                        offer.conditions().change_before_departure().allowed()
                                ) : null
                        ) : null,
                        offer.available_services(),
                        offer.allowed_passenger_identity_document_types()
                )
        ).toList();
        log.info("Returning {} flights", flights.size());
        return new SearchResponse(flights);
    }

    @Tool(name = "bookFlight", description = "Book a selected flight offer using Duffel API. Requires flightId and passenger details.")
    public BookingResponse bookFlight(BookingRequest request) {
        log.info("Booking flight for flightId={}, passengers={}", request.flightId(), request.passengers().size());
        var duffelPassengers = request.passengers().stream().map(p ->
                new DuffelBookFlightRequest.Passenger(
                        p.id() != null ? p.id() : "", // never null
                        p.phoneNumber() != null ? p.phoneNumber() : "",
                        p.email() != null ? p.email() : "",
                        p.bornOn() != null ? p.bornOn() : "",
                        p.title() != null ? p.title() : "",
                        p.gender() != null ? p.gender() : "",
                        p.familyName() != null ? p.familyName() : "",
                        p.givenName() != null ? p.givenName() : "",
                        p.infant_passenger_id() != null ? p.infant_passenger_id() : ""
                )
        ).toList();
        var duffelRequest = new DuffelBookFlightRequest(
                new DuffelBookFlightRequest.Data(
                        List.of(request.flightId()),
                        List.of(new DuffelBookFlightRequest.Payment("balance", request.amount(), request.currency())),
                        duffelPassengers
                )
        );
        log.info("Duffel bookFlight request payload: {}", duffelRequest);
        DuffelBookFlightResponse duffelResponse = duffelClient.bookFlight(duffelRequest);
        log.debug("Duffel book response: {}", duffelResponse);
        var bookingResponse = new BookingResponse(
                duffelResponse.data().id(),
                "BOOKED",
                Double.parseDouble(duffelResponse.data().total_amount()),
                duffelResponse.data().total_currency()
        );
        log.info("Booking completed: bookingId={}", bookingResponse.bookingId());
        return bookingResponse;
    }

    @Tool(name = "payForFlight", description = "Pay for a booked flight using Duffel API. Requires bookingId and payment token.")
    public PaymentResponse payForFlight(PaymentRequest request) {
        log.info("Paying for flight: bookingId={}", request.bookingId());
        var payment = new DuffelPayForFlightRequest.Payment("balance", request.amount(), request.currency());
        var data = new DuffelPayForFlightRequest.Data(
                request.bookingId() != null ? request.bookingId() : "",
                payment
        );
        var duffelRequest = new DuffelPayForFlightRequest(data);
        log.info("Duffel payForFlight request payload: {}", duffelRequest);
        DuffelPayForFlightResponse duffelResponse = duffelClient.payForFlight(duffelRequest);
        log.debug("Duffel payment response: {}", duffelResponse);
        var paymentResponse = new PaymentResponse(
                duffelResponse.data().status(),
                duffelResponse.data().id()
        );
        log.info("Payment completed: paymentReference={}", paymentResponse.paymentReference());
        return paymentResponse;
    }
}
