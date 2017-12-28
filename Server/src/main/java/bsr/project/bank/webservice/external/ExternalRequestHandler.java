package bsr.project.bank.webservice.external;

import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.service.AccountOperationService;
import bsr.project.bank.service.ExternalAuthorizationService;
import bsr.project.bank.utility.logging.LogMethodCall;
import bsr.project.bank.webservice.external.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalRequestHandler {

    @Autowired
    private ExternalRequestValidator validator;

    @Autowired
    private ExternalAuthorizationService authorizationService;

    @Autowired
    private AccountOperationService accountOperationService;

    @LogMethodCall
    public void handleRequest(String accountNumber, ExternalTransfer transfer, String authorizationHeader)
            throws AccountDoesNotExistsException, InvalidAmountException,
            InvalidTitleException, InvalidSenderNameException, InvalidSourceAccountException, InvalidCredentialsException {
        checkAuthentication(authorizationHeader);
        validateRequest(accountNumber, transfer);
        accountOperationService.transfer(transfer, accountNumber);
    }

    private void checkAuthentication(String authorizationHeader) throws InvalidCredentialsException {
        authorizationService.authenticate(authorizationHeader);
    }

    private void validateRequest(String accountNumber, ExternalTransfer transfer)
            throws AccountDoesNotExistsException, InvalidAmountException,
            InvalidTitleException, InvalidSenderNameException, InvalidSourceAccountException {
        validator.validate(accountNumber);
        validator.validate(transfer);
    }
}
