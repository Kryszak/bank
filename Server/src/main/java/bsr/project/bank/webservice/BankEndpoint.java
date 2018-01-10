package bsr.project.bank.webservice;

import bsr.project.bank.model.User;
import bsr.project.bank.model.exception.AuthenticationFailedException;
import bsr.project.bank.model.exception.DestinationAccountNotFoundException;
import bsr.project.bank.model.exception.UnknownErrorException;
import bsr.project.bank.model.exception.ValidationErrorException;
import bsr.project.bank.service.AccountOperationService;
import bsr.project.bank.service.AccountsService;
import bsr.project.bank.service.InternalOperationValidator;
import bsr.project.bank.service.validation.*;
import com.bsr.types.bank.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Endpoint
public class BankEndpoint {

    @Autowired
    private InternalOperationValidator validator;

    @Autowired
    private AccountOperationService accountOperationService;

    @Autowired
    private AccountsService accountsService;

    private ObjectFactory factory = new ObjectFactory();

    private static final String NAMESPACE_URI = "http://bsr.com/types/bank";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
    @ResponsePayload
    public LoginResponse login(@SoapHeader("{http://bsr.com/types/bank}AuthHeader") SoapHeaderElement auth) throws JAXBException, AuthenticationFailedException {
        User user = authenticate(auth);
        return getLoginResponse(user);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AccountHistoryRequest")
    @ResponsePayload
    public AccountHistoryResponse accountHistory(@RequestPayload AccountHistoryRequest payload,
                                                 @SoapHeader("{http://bsr.com/types/bank}AuthHeader") SoapHeaderElement auth)
            throws InvalidSourceAccountException, AccountDoesNotExistsException, JAXBException, AuthenticationFailedException {
        User user = authenticate(auth);

        validator.validate(payload, user);
        List<AccountHistoryElement> accountHistoryElements = accountOperationService.getAccountHistory(payload);

        AccountHistoryResponse response = new AccountHistoryResponse();
        response.getAccountHistory().addAll(accountHistoryElements);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InternalTransferRequest")
    @ResponsePayload
    public OperationSuccessResponse internalTransfer(@RequestPayload InternalTransferRequest payload,
                                                     @SoapHeader("{http://bsr.com/types/bank}AuthHeader") SoapHeaderElement auth)
            throws InvalidSourceAccountException, InvalidAmountException,
            InvalidTitleException, InvalidDestinationAccountException, AccountDoesNotExistsException, JAXBException, AuthenticationFailedException {
        User user = authenticate(auth);

        validator.validate(payload, user);
        accountOperationService.internalTransfer(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Przelew przyjęty.");

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "PaymentRequest")
    @ResponsePayload
    public OperationSuccessResponse payment(@RequestPayload PaymentRequest payload,
                                            @SoapHeader("{http://bsr.com/types/bank}AuthHeader") SoapHeaderElement auth)
            throws InvalidAmountException, InvalidDestinationAccountException, AccountDoesNotExistsException, JAXBException, AuthenticationFailedException, InvalidSourceAccountException {
        User user = authenticate(auth);

        validator.validate(payload, user);
        accountOperationService.remittance(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Wpłata przyjęta.");

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "WithdrawalRequest")
    @ResponsePayload
    public OperationSuccessResponse withdrawal(@RequestPayload WithdrawalRequest payload,
                                               @SoapHeader("{http://bsr.com/types/bank}AuthHeader") SoapHeaderElement auth)
            throws InvalidAmountException, InvalidDestinationAccountException, AccountDoesNotExistsException, JAXBException, AuthenticationFailedException, InvalidSourceAccountException {
        User user = authenticate(auth);

        validator.validate(payload, user);
        accountOperationService.withdrawal(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Wypłacono pieniądze");

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ExternalTransferRequest")
    @ResponsePayload
    public OperationSuccessResponse externalTransfer(@RequestPayload ExternalTransferRequest payload,
                                                     @SoapHeader("{http://bsr.com/types/bank}AuthHeader") SoapHeaderElement auth)
            throws IOException,
            AuthenticationFailedException,
            DestinationAccountNotFoundException,
            UnknownErrorException,
            ValidationErrorException,
            InvalidSourceAccountException,
            InvalidAmountException,
            InvalidTitleException,
            InvalidDestinationAccountException, AccountDoesNotExistsException {
        User user = authenticate(auth);

        validator.validate(payload, user);
        accountOperationService.externalTransfer(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Przelew przyjęty.");

        return response;
    }

    private User authenticate(SoapHeaderElement auth) throws AuthenticationFailedException {
        AuthHeader authentication;
        try {
            JAXBContext context = JAXBContext.newInstance(AuthHeader.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            authentication = (AuthHeader) unmarshaller.unmarshal(auth.getSource());
        } catch (Exception e) {
            throw new AuthenticationFailedException();
        }
        return validator.checkAuthenticated(authentication);
    }

    private LoginResponse getLoginResponse(User user) {
        LoginResponse response = factory.createLoginResponse();
        response.getAccounts().addAll(accountsService.getUserAccounts(user).stream().map(account -> {
            AccountsElement acc = factory.createAccountsElement();
            acc.setAccountNumber(account.getAccountNumber());
            acc.setBalance((int) account.getBalance());
            return acc;
        }).collect(Collectors.toList()));
        return response;
    }
}
