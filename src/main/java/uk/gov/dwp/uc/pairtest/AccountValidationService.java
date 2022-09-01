package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;

public interface AccountValidationService {
    void validateAccountId(long accountId) throws InvalidAccountException;
}
