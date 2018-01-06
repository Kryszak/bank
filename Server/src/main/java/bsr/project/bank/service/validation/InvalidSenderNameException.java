package bsr.project.bank.service.validation;

import lombok.NoArgsConstructor;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@NoArgsConstructor
@SoapFault(faultCode = FaultCode.CLIENT, faultStringOrReason = "Błędna nazwa zlecającego.")
public class InvalidSenderNameException extends Exception {

}
