package bsr.project.bank.webservice.external.validation;

import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.service.AbstractRequestsValidator;
import bsr.project.bank.service.validation.*;
import bsr.project.bank.utility.logging.LogMethodCall;
import org.springframework.stereotype.Service;

@Service
public class ExternalRequestValidator extends AbstractRequestsValidator {

    @LogMethodCall
    public void validate(ExternalTransfer transfer)
            throws InvalidAmountException, InvalidTitleException, InvalidSenderNameException, InvalidSourceAccountException, AccountDoesNotExistsException {
        validateAmount(transfer.getAmount());
        validateTitle(transfer.getTitle());
        validateName(transfer.getName());
        validateSourceAccount(transfer.getSource_account());
    }


}
