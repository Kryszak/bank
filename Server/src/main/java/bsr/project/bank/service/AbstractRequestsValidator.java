package bsr.project.bank.service;

import bsr.project.bank.model.User;
import bsr.project.bank.service.validation.*;
import org.iban4j.IbanUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRequestsValidator {

    @Autowired
    protected AccountsService accountsService;

    protected void validateSourceAccount(String sourceAccount) throws InvalidSourceAccountException, AccountDoesNotExistsException {
        if (sourceAccount == null || sourceAccount.isEmpty()) {
            throw new InvalidSourceAccountException("Nie podano numeru konta zlecającego.");
        }
        if (sourceAccount.length() != 26) {
            throw new InvalidSourceAccountException("Numer rachunku musi zawierać 26 cyfr.");
        }
        validate(sourceAccount);
        try {
            IbanUtil.validate("PL" + sourceAccount);
        } catch (Exception e) {
            throw new InvalidSourceAccountException("Błędny numer rachunku.");
        }
    }

    protected void validateUserAccount(String account, User user) throws InvalidSourceAccountException {
        User accountUser = accountsService.getAccount(account).getUser();
        if (!accountUser.equals(user)) {
            throw new InvalidSourceAccountException("Konto nie należy do tego użytkownika");
        }
    }

    protected void validateDestinationAccount(String destinationAccount) throws InvalidDestinationAccountException, AccountDoesNotExistsException {
        if (destinationAccount == null || destinationAccount.isEmpty()) {
            throw new InvalidDestinationAccountException("Nie podano numeru konta docelowego.");
        }
        if (destinationAccount.length() != 26) {
            throw new InvalidDestinationAccountException("Numer rachunku musi zawierać 26 cyfr.");
        }
        validate(destinationAccount);
        try {
            IbanUtil.validate("PL" + destinationAccount);
        } catch (Exception e) {
            throw new InvalidDestinationAccountException("Błędny numer rachunku.");
        }
    }

    public void validate(String accountNumber) throws AccountDoesNotExistsException {
        if (!accountsService.accountExists(accountNumber)) {
            throw new AccountDoesNotExistsException();
        }
    }

    protected void validateName(String name) throws InvalidSenderNameException {
        if (name == null || name.isEmpty()) {
            throw new InvalidSenderNameException();
        }
    }

    protected void validateTitle(String title) throws InvalidTitleException {
        if (title == null || title.isEmpty()) {
            throw new InvalidTitleException("nie podano tytułu przelewu");
        }
        if (title.length() > 255) {
            throw new InvalidTitleException("podany tytuł jest dłuższy niż 255 znaków.");
        }
    }

    protected void validateAmount(Integer amount, String account) throws InvalidAmountException {
        validateAmount(amount);
        if (accountsService.getAccount(account).getBalance() < amount) {
            throw new InvalidAmountException();
        }
    }

    protected void validateAmount(Integer amount) throws InvalidAmountException {
        if (amount == null || amount < 1) {
            throw new InvalidAmountException();
        }
    }
}
