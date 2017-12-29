package bsr.project.bank.webservice;

import bsr.project.bank.service.AccountOperationService;
import bsr.project.bank.service.InternalOperationValidator;
import com.bsr.types.bank.InternalTransferRequest;
import com.bsr.types.bank.ObjectFactory;
import com.bsr.types.bank.OperationSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
public class BankEndpoint {

    @Autowired
    private InternalOperationValidator validator;

    @Autowired
    private AccountOperationService accountOperationService;

    @PayloadRoot(namespace = "http://bsr.com/types/bank", localPart = "InternalTransferRequest")
    @ResponsePayload
    public OperationSuccessResponse internalTransfer(@RequestPayload InternalTransferRequest payload) {

        accountOperationService.internalTransfer(payload);

        ObjectFactory factory = new ObjectFactory();
        OperationSuccessResponse response = factory.createOperationSuccessResponse();

        response.setMessage("Sukces!");

        return response;
    }
}
