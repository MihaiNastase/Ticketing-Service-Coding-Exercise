package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import static org.junit.Assert.*;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.AccountValidationServiceImpl;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.HashMap;

public class TicketServiceTests_helperMethods {
    private final TicketService testTicketService = new TicketServiceImpl(new TicketPaymentServiceImpl(), new SeatReservationServiceImpl(), new AccountValidationServiceImpl());
    private final TicketTypeRequest adultTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4);
    private final TicketTypeRequest childTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 6);
    private final TicketTypeRequest infantTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 4);


    @Test
    public void aggregateTicketsPerType_oneRequestOfEachType_shouldCorrectlyReturnCounts() {
        HashMap<String, Integer> testCollectorMap = testTicketService.aggregateRequests(adultTicketRequest, childTicketRequest, infantTicketRequest);
        int adultTickets = testCollectorMap.get(TicketTypeRequest.Type.ADULT.name());
        int childTickets = testCollectorMap.get(TicketTypeRequest.Type.CHILD.name());
        int infantTickets = testCollectorMap.get(TicketTypeRequest.Type.INFANT.name());

        assertEquals(4, adultTickets);
        assertEquals(6, childTickets);
        assertEquals(4, infantTickets);
    }

    @Test
    public void aggregateTicketsPerType_multipleRequestOfEachType_shouldCorrectlyReturnCounts() {
        HashMap<String, Integer> testCollectorMap = testTicketService.aggregateRequests(adultTicketRequest, childTicketRequest, infantTicketRequest, adultTicketRequest, childTicketRequest);
        int adultTickets = testCollectorMap.get(TicketTypeRequest.Type.ADULT.name());
        int childTickets = testCollectorMap.get(TicketTypeRequest.Type.CHILD.name());
        int infantTickets = testCollectorMap.get(TicketTypeRequest.Type.INFANT.name());

        assertEquals(8, adultTickets);
        assertEquals(12, childTickets);
        assertEquals(4, infantTickets);
    }

    @Test
    public void calculateTotalPrice_multipleRequestOfEachType_shouldCorrectlyReturnCounts() {
        HashMap<String, Integer> testCollectorMap = testTicketService.aggregateRequests(adultTicketRequest, childTicketRequest, infantTicketRequest, adultTicketRequest, childTicketRequest);
        int adultTickets = testCollectorMap.get(TicketTypeRequest.Type.ADULT.name());
        int childTickets = testCollectorMap.get(TicketTypeRequest.Type.CHILD.name());
        int infantTickets = testCollectorMap.get(TicketTypeRequest.Type.INFANT.name());

        int total = testTicketService.computeTotalTicketsPrice(adultTickets, childTickets, infantTickets);

        assertEquals(280, total);
    }
}
