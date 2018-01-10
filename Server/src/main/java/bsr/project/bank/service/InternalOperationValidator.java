package bsr.project.bank.service;

import bsr.project.bank.model.User;
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

    public User checkAuthenticated(AuthHeader authHeader) throws AuthenticationFailedException {
        return userService.authenticate(authHeader);
    }

    @LogMethodCall
    public void validate(AccountHistoryRequest request, User user) throws InvalidSourceAccountException, AccountDoesNotExistsException {
        validateSourceAccount(request.getAccount());
        validateUserAccount(request.getAccount(), user);
    }

    @LogMethodCall
    public void validate(InternalTransferRequest request, User user)
            throws InvalidAmountException,
            InvalidSourceAccountException,
            InvalidTitleException,
            InvalidDestinationAccountException, AccountDoesNotExistsException {
        validateTransfer(user, request.getAmount(), request.getSourceAccount(), request.getTitle(), request.getDestinationAccount());
    }

    @LogMethodCall
    public void validate(PaymentRequest request, User user) throws InvalidDestinationAccountException,
            InvalidAmountException, AccountDoesNotExistsException, InvalidSourceAccountException {
        validateAmount(request.getAmount());
        validateDestinationAccount(request.getDestinationAccount());
        validateUserAccount(request.getDestinationAccount(), user);
    }

    @LogMethodCall
    public void validate(WithdrawalRequest request, User user) throws InvalidDestinationAccountException,
            InvalidAmountException, AccountDoesNotExistsException, InvalidSourceAccountException {
        validateAmount(request.getAmount(), request.getDestinationAccount());
        validateDestinationAccount(request.getDestinationAccount());
        validateUserAccount(request.getDestinationAccount(), user);
    }

    @LogMethodCall
    public void validate(ExternalTransferRequest request, User user) throws
            InvalidAmountException, InvalidSourceAccountException,
            InvalidTitleException, InvalidDestinationAccountException, AccountDoesNotExistsException {
        validateTransfer(user, request.getAmount(), request.getSourceAccount(), request.getTitle(), request.getDestinationAccount());
    }

    private void validateTransfer(User user, int amount, String sourceAccount, String title, String destinationAccount) throws InvalidAmountException, InvalidSourceAccountException, AccountDoesNotExistsException, InvalidTitleException, InvalidDestinationAccountException {
        validateAmount(amount, sourceAccount);
        validateSourceAccount(sourceAccount);
        validateUserAccount(sourceAccount, user);
        validateTitle(title);
        validateDestinationAccount(destinationAccount);
    }
}
