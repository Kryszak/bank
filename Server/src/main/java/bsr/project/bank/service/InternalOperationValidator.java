package bsr.project.bank.service;

import bsr.project.bank.model.exception.AuthenticationFailedException;
import bsr.project.bank.service.validation.*;
import bsr.project.bank.utility.logging.LogMethodCall;
import com.bsr.types.bank.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InternalOperationValidator extends AbstractRequestsValidator {

    @Autowired
    private UserService userService;

    public void checkAuthenticated(AuthHeader authHeader) throws AuthenticationFailedException {
        userService.authenticate(authHeader);
    }

    @LogMethodCall
    public void validate(AccountHistoryRequest request) throws InvalidSourceAccountException, AccountDoesNotExistsException {
        validateSourceAccount(request.getAccount());
    }

    @LogMethodCall
    public void validate(InternalTransferRequest request)
            throws InvalidAmountException,
            InvalidSourceAccountException,
            InvalidTitleException,
            InvalidDestinationAccountException, AccountDoesNotExistsException {
        validateAmount(request.getAmount());
        validateSourceAccount(request.getSourceAccount());
        validateTitle(request.getTitle());
        validateDestinationAccount(request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(PaymentRequest request) throws InvalidDestinationAccountException,
            InvalidAmountException, AccountDoesNotExistsException {
        validateAmount(request.getAmount());
        validateDestinationAccount(request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(WithdrawalRequest request) throws InvalidDestinationAccountException,
            InvalidAmountException, AccountDoesNotExistsException {
        validateAmount(request.getAmount());
        validateDestinationAccount(request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(ExternalTransferRequest request) throws
            InvalidAmountException, InvalidSourceAccountException,
            InvalidTitleException, InvalidDestinationAccountException, AccountDoesNotExistsException {
        validateAmount(request.getAmount());
        validateSourceAccount(request.getSourceAccount());
        validateTitle(request.getTitle());
        validateDestinationAccount(request.getDestinationAccount());
    }
}
