package bsr.project.bank.webservice;

import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.webservice.external.ExternalRequestHandler;
import bsr.project.bank.webservice.external.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ExternalTransferController {

    @Autowired
    private ExternalRequestHandler handler;

    @RequestMapping(value = "/accounts/{accountNumber}/history", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void handleExternalTransfer(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                       @PathVariable("accountNumber") String accountNumber,
                                       @RequestBody ExternalTransfer transfer) throws AccountDoesNotExistsException,
            InvalidAmountException, InvalidTitleException, InvalidSenderNameException, InvalidSourceAccountException,
            InvalidCredentialsException {
        log.info(">>> Handling external transfer {} to account {}", transfer, accountNumber);
        handler.handleRequest(accountNumber, transfer, authorizationHeader);
        log.info("<<< External transfer handled");
    }
}
