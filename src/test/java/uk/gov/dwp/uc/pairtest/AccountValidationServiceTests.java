package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.gov.dwp.uc.pairtest.AccountValidationService;
import uk.gov.dwp.uc.pairtest.AccountValidationServiceImpl;
import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;

public class AccountValidationServiceTests {
    private final AccountValidationService testAccountValidationService = new AccountValidationServiceImpl();

    @Test
    public void validateAccountId_IdIsGreaterThanZero_shouldDoNothing(){
        try{
            this.testAccountValidationService.validateAccountId(12345);
        } catch (InvalidAccountException ex) {
            fail("Validation flagged valid Id as invalid; Exception was thrown...");
        }
    }

    @Test
    public void validateAccountId_IdIsLessThanZero_shouldThrowException(){
        try{
            this.testAccountValidationService.validateAccountId(-12345);
            fail("Validation did not flag negative Id; No thrown exception...");
        } catch (InvalidAccountException ex) {}
    }
}
