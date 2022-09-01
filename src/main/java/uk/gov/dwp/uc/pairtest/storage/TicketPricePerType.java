package uk.gov.dwp.uc.pairtest.storage;
/**
 *  Enum used to simulate the data source for ticket prices.
 *  <p>While hard-coded in this scenario, this data can come from other sources (an database read for example) in a real implementation.</p>
 */
public enum TicketPricePerType {
    INFANT(0),
    CHILD(10),
    ADULT(20);

    public final int price;
    private TicketPricePerType(int price) {
        this.price = price;
    }
}
