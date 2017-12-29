package bsr.project.bank.webservice.external.validation;

import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.service.AbstractRequestsValidator;
import bsr.project.bank.service.validation.InvalidAmountException;
import bsr.project.bank.service.validation.InvalidSenderNameException;
import bsr.project.bank.service.validation.InvalidSourceAccountException;
import bsr.project.bank.service.validation.InvalidTitleException;
import bsr.project.bank.utility.logging.LogMethodCall;
import org.springframework.stereotype.Service;

@Service
public class ExternalRequestValidator extends AbstractRequestsValidator {

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


}
