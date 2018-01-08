package bsr.project.bank.model.exception;

import lombok.NoArgsConstructor;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@NoArgsConstructor
@SoapFault(faultCode = FaultCode.CLIENT, faultStringOrReason = "Błąd autoryzacji.")
public class AuthenticationFailedException extends Exception {
}
