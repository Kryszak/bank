package bsr.project.bank.service.validation;

import lombok.NoArgsConstructor;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@NoArgsConstructor
@SoapFault(faultCode = FaultCode.CLIENT, faultStringOrReason = "Podane konto nie istnieje.")
public class AccountDoesNotExistsException extends Exception {

}
