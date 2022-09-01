package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.exception.InvalidAccountException;

/**
 * Handler class for validating user accounts.
 */
public class AccountValidationServiceImpl implements AccountValidationService{
    @Override
    public void validateAccountId(long accountId) throws InvalidAccountException {
        /**
         * Method for directly validating an account Id.
         * <p>Checks against a set of rules that verify if a received account Id is valid or not.</p>
         * @param accountId Unique identifier representing a user account.
         * @throws {@link InvalidAccountException}
         * @return Function returns nothing if validation is successful, throws {@link InvalidAccountException} otherwise.
         */

        if(accountId < 0){
            throw new InvalidAccountException("Account Id must be greater than zero.");
        }
    }
}
