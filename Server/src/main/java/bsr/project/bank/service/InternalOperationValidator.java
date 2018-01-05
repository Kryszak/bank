package bsr.project.bank.service;

import bsr.project.bank.service.validation.InvalidAmountException;
import bsr.project.bank.service.validation.InvalidDestinationAccountException;
import bsr.project.bank.service.validation.InvalidSourceAccountException;
import bsr.project.bank.service.validation.InvalidTitleException;
import bsr.project.bank.utility.logging.LogMethodCall;
import com.bsr.types.bank.*;
import org.springframework.stereotype.Service;

@Service
public class InternalOperationValidator extends AbstractRequestsValidator {

    @LogMethodCall
    public void checkAuthenticated() {

    }

    @LogMethodCall
    public void validate(AccountHistoryRequest request) throws InvalidSourceAccountException {
        validateSourceAccount(request.getAccount());
    }

    @LogMethodCall
    public void validate(InternalTransferRequest request)
            throws InvalidAmountException,
            InvalidSourceAccountException,
            InvalidTitleException,
            InvalidDestinationAccountException {
        validateAmount(request.getAmount());
        validateSourceAccount(request.getSourceAccount());
        validateTitle(request.getTitle());
        validateDestinationAccount(request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(PaymentRequest request) throws InvalidDestinationAccountException,
            InvalidAmountException {
        validateAmount(request.getAmount());
        validateDestinationAccount(request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(WithdrawalRequest request) throws InvalidDestinationAccountException,
            InvalidAmountException {
        validateAmount(request.getAmount());
        validateDestinationAccount(request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(ExternalTransferRequest request) throws
            InvalidAmountException, InvalidSourceAccountException,
            InvalidTitleException, InvalidDestinationAccountException {
        validateAmount(request.getAmount());
        validateSourceAccount(request.getSourceAccount());
        validateTitle(request.getTitle());
        validateDestinationAccount(request.getDestinationAccount());
    }
}
