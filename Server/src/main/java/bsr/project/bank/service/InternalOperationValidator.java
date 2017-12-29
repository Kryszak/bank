package bsr.project.bank.service;

import bsr.project.bank.service.validation.InvalidDestinationAccountException;
import bsr.project.bank.utility.logging.LogMethodCall;
import bsr.project.bank.service.validation.InvalidAmountException;
import bsr.project.bank.service.validation.InvalidSourceAccountException;
import bsr.project.bank.service.validation.InvalidTitleException;
import com.bsr.types.bank.AccountHistoryRequest;
import com.bsr.types.bank.InternalTransferRequest;
import com.bsr.types.bank.PaymentRequest;
import com.bsr.types.bank.WithdrawalRequest;
import org.springframework.stereotype.Service;

@Service
public class InternalOperationValidator extends AbstractRequestsValidator {

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
    public void validate(PaymentRequest request){
        // TODO
    }

    @LogMethodCall
    public void validate(WithdrawalRequest request){
        // TODO
    }
}
