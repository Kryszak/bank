package bsr.project.bank.webservice;

import bsr.project.bank.service.AccountOperationService;
import bsr.project.bank.service.InternalOperationValidator;
import com.bsr.types.bank.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Slf4j
@Endpoint
public class BankEndpoint {

    @Autowired
    private InternalOperationValidator validator;

    @Autowired
    private AccountOperationService accountOperationService;

    private ObjectFactory factory = new ObjectFactory();

    @PayloadRoot(namespace = "http://bsr.com/types/bank", localPart = "AccountHistoryRequest")
    @ResponsePayload
    public AccountHistoryResponse accountHistory(@RequestPayload AccountHistoryRequest payload) {
        List<AccountHistoryElement> accountHistoryElements = accountOperationService.getAccountHistory(payload);

        AccountHistoryResponse response = new AccountHistoryResponse();

        response.getAccountHistory().addAll(accountHistoryElements);

        return response;
    }

    @PayloadRoot(namespace = "http://bsr.com/types/bank", localPart = "InternalTransferRequest")
    @ResponsePayload
    public OperationSuccessResponse internalTransfer(@RequestPayload InternalTransferRequest payload) {
        accountOperationService.internalTransfer(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Przelew przyjęty.");

        return response;
    }

    @PayloadRoot(namespace = "http://bsr.com/types/bank", localPart = "PaymentRequest")
    @ResponsePayload
    public OperationSuccessResponse payment(@RequestPayload PaymentRequest payload) {
        accountOperationService.remittance(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Wpłata przyjęta.");

        return response;
    }

    @PayloadRoot(namespace = "http://bsr.com/types/bank", localPart = "WithdrawalRequest")
    @ResponsePayload
    public OperationSuccessResponse withdrawal(@RequestPayload WithdrawalRequest payload) {
        accountOperationService.withdrawal(payload);

        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Wypłacono pieniądze");

        return response;
    }
}
