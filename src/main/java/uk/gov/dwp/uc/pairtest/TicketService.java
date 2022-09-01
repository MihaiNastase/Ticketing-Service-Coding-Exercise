package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.HashMap;

public interface TicketService {

    void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException;
    HashMap<String, Integer> aggregateRequests(TicketTypeRequest... ticketTypeRequests);
    int computeTotalTicketsPrice(int adultTicketsNumber, int childTicketsNumber, int infantTicketsNumber);
}
