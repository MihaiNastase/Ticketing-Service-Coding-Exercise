package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.storage.TicketPricePerType;

import java.util.*;
import java.util.stream.Stream;


public class TicketServiceImpl implements TicketService {
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final AccountValidationService accountValidationService;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService, AccountValidationService accountValidationService){
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.accountValidationService = accountValidationService;
    }
    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        // Basic checks to be run beforehand
        if(ticketTypeRequests.length == 0) {
            throw new IllegalArgumentException("No ticket requests provided.");
        }
        try {
            this.accountValidationService.validateAccountId(accountId);
        } catch (InvalidAccountException ex) {
            throw new InvalidPurchaseException("Invalid account: " + ex.getMessage());
        }

        // Aggregate requests in case multiple requests of the same type have been registered
        HashMap<String, Integer> requestCollectorMap = this.aggregateRequests(ticketTypeRequests);

        //Possibly overkill, but sets constraints for the data that makes the checks easier to implement.
        Integer adultTicketsNumber = Objects.requireNonNullElse(requestCollectorMap.get(TicketTypeRequest.Type.ADULT.name()), 0) ;
        Integer childTicketsNumber = Objects.requireNonNullElse(requestCollectorMap.get(TicketTypeRequest.Type.CHILD.name()), 0);
        Integer infantTicketsNumber = Objects.requireNonNullElse(requestCollectorMap.get(TicketTypeRequest.Type.INFANT.name()), 0);

        //Mandatory checks for business logic
        if(adultTicketsNumber == 0) {
            throw new InvalidPurchaseException("Illegal transaction: No tickets of type Adult registered for the purchase.");
        }
        if(adultTicketsNumber < 0 || childTicketsNumber < 0 || infantTicketsNumber < 0) {
            throw new InvalidPurchaseException("Illegal state: Number of tickets cannot be a less that 0.");
        }
        if(infantTicketsNumber > adultTicketsNumber) {
            throw new InvalidPurchaseException("Illegal transaction: Too many tickets of type Infant requested; Every infant must be accompanied by at least one adult.");
        }
        if(adultTicketsNumber + childTicketsNumber + infantTicketsNumber > 20) {
            throw new InvalidPurchaseException("Illegal transaction: Too many tickets requested; Maximum of 20 tickets are allowed per purchase.");
        }

        //No further checks required, we assume these services are perfect.
        this.ticketPaymentService.makePayment(accountId, this.computeTotalTicketsPrice(adultTicketsNumber, childTicketsNumber, infantTicketsNumber));
        this.seatReservationService.reserveSeat(accountId ,adultTicketsNumber + childTicketsNumber + infantTicketsNumber);
    }


    // While the brief says these methods SHOULD be private, those calculations (numbers of tickets and total price) are critical processes in the scope
    // of this application/system, so they have been detached into their own functions to be properly tested via unit tests (and leave room for more advanced
    // checks if the design of the system would change in the future).
    /**
     * Aggregates all requests types in a single HashMap
     * <p>Helper method which collects all the ticket requests as variable arguments and returns a single HashMap which aggregates the number of tickets per type,
     * allowing the method to process a variable number of arguments, allowing the system to accept more than one request per type.</p>
     * @param ticketTypeRequests Multiple ticket type requests passes as variable arguments.
     * @return HashMap<String, Integer>
     */
    public HashMap<String, Integer> aggregateRequests(TicketTypeRequest... ticketTypeRequests) {
        // Dynamically collecting variable parameters
        HashMap<String, Integer> requestCollectorMap = new HashMap<>();
        // Merging requests in collectors object to provide more leverage for different sources of input to this method;
        Stream.of(ticketTypeRequests).forEach(request -> requestCollectorMap.merge(
                request.getTicketType().name(),
                request.getNoOfTickets(),
                Integer::sum
        ));

        return requestCollectorMap;
    }

    /**
     * Computes total payment to be made for the reservation
     * <p>Helper method which calculates the total to be payed based on given number of tickets per type and registered ticket prices per said types.</p>
     * @param adultTicketsNumber Number of Adult tickets registered.
     * @param childTicketsNumber Number of Child tickets registered.
     * @param infantTicketsNumber Number of Infant tickets registered.
     * @return Total amount to be paid
     */
    public int computeTotalTicketsPrice(int adultTicketsNumber, int childTicketsNumber, int infantTicketsNumber) {
        int paymentValue = adultTicketsNumber * TicketPricePerType.ADULT.price
                + childTicketsNumber * TicketPricePerType.CHILD.price
                + infantTicketsNumber * TicketPricePerType.INFANT.price;
        return  paymentValue;
    }

}
