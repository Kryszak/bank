package bsr.project.bank.webservice.external.validation;

import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.service.AccountsService;
import bsr.project.bank.utility.logging.LogMethodCall;
import org.iban4j.IbanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalRequestValidator {

    @Autowired
    private AccountsService accountsService;

    @LogMethodCall
    public void validate(String accountNumber) throws AccountDoesNotExistsException {
        if (!accountsService.accountExists(accountNumber)) {
            throw new AccountDoesNotExistsException();
        }
    }

    @LogMethodCall
    public void validate(ExternalTransfer transfer)
            throws InvalidAmountException, InvalidTitleException, InvalidSenderNameException, InvalidSourceAccountException {
        validateAmount(transfer.getAmount());
        validateTitle(transfer.getTitle());
        validateName(transfer.getName());
        validateSourceAccount(transfer.getSource_account());
    }

    private void validateSourceAccount(String sourceAccount) throws InvalidSourceAccountException {
        if (sourceAccount == null || sourceAccount.isEmpty()) {
            throw new InvalidSourceAccountException("Nie podano numeru konta zlecającego.");
        }
        if (sourceAccount.length() != 26) {
            throw new InvalidSourceAccountException("Numer rachunku musi zawierać 26 cyfr.");
        }
        try {
            IbanUtil.validate("PL" + sourceAccount);
        } catch (Exception e) {
            throw new InvalidSourceAccountException("Błędny numer rachunku.");
        }
    }

    private void validateName(String name) throws InvalidSenderNameException {
        if (name == null || name.isEmpty()) {
            throw new InvalidSenderNameException();
        }
    }

    private void validateTitle(String title) throws InvalidTitleException {
        if (title == null || title.isEmpty()) {
            throw new InvalidTitleException("nie podano tytułu przelewu");
        }
        if (title.length() > 255) {
            throw new InvalidTitleException("podany tytuł jest dłuższy niż 255 znaków.");
        }
    }

    private void validateAmount(Integer amount) throws InvalidAmountException {
        if (amount == null || amount < 1) {
            throw new InvalidAmountException();
        }
    }
}
