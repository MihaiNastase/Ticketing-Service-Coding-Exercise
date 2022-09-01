package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import static org.junit.Assert.*;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.AccountValidationServiceImpl;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceTests_purchaseTickets {
    private final TicketService testTicketService = new TicketServiceImpl(new TicketPaymentServiceImpl(), new SeatReservationServiceImpl(), new AccountValidationServiceImpl());

    private final TicketTypeRequest validAdultTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4);
    private final TicketTypeRequest invalidAdultTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);
    private final TicketTypeRequest manyAdultTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);

    private final TicketTypeRequest validChildTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 6);
    private final TicketTypeRequest invalidChildTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, -1);
    private final TicketTypeRequest manyChildTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10);

    private final TicketTypeRequest validInfantTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 4);
    private final TicketTypeRequest invalidInfantTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, -1);
    private final TicketTypeRequest manyInfantTicketRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 11);


    @Test
    public void reserveAdultSeats_validAdultTicketRequest_shouldProcessRequest() {
        try {
            testTicketService.purchaseTickets(12345L, validAdultTicketRequest);
        } catch (InvalidPurchaseException ex) {
            fail(ex.getMessage());
        }
    }
    @Test
    public void reserveAdultSeats_invalidAdultTicketRequest_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L, invalidAdultTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }


    @Test
    public void reserveAdultAndChildSeats_BothRequestsValid_shouldProcessRequest() {
        try {
            testTicketService.purchaseTickets(12345L, validAdultTicketRequest, validChildTicketRequest);
        } catch (InvalidPurchaseException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void reserveChildSeats_NoAdultTicketsRequested_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L,validChildTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }

    @Test
    public void reserveAdultAndChildSeats_InvalidChildRequest_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L,validAdultTicketRequest, invalidChildTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }

    @Test
    public void reserveAdultAndInfantSeats_EqualInfantsAndAdults_shouldProcessRequest() {
        try {
            testTicketService.purchaseTickets(12345L,validAdultTicketRequest, validInfantTicketRequest);
        } catch (InvalidPurchaseException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void reserveAdultAndInfantSeats_LessInfantsThanAdults_shouldProcessRequest() {
        try {
            testTicketService.purchaseTickets(12345L,manyAdultTicketRequest, validInfantTicketRequest);
        } catch (InvalidPurchaseException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void reserveAdultAndInfantSeats_NoAdultTicketsRequested_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L,validInfantTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }

    @Test
    public void reserveAdultAndInfantSeats_MoreInfantsThanAdults_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L,manyInfantTicketRequest, validAdultTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }

    @Test
    public void reserveAdultAndInfantSeats_InvalidInfantRequest_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L,validAdultTicketRequest, invalidInfantTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }

    @Test
    public void reserveAllTicketTypes_AllValidRequests_shouldProcessRequest() {
        try {
            testTicketService.purchaseTickets(12345L,validAdultTicketRequest, validChildTicketRequest, validInfantTicketRequest);
        } catch (InvalidPurchaseException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void reserveAllTicketTypes_TooManySeats_shouldThrowException() {
        try {
            testTicketService.purchaseTickets(12345L,manyAdultTicketRequest, manyInfantTicketRequest, manyChildTicketRequest);
            fail("Exception not caught.");
        } catch (InvalidPurchaseException ex) {}
    }
}
