package bsr.project.bank.service.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@AllArgsConstructor
@SoapFault(faultCode = FaultCode.CLIENT, faultStringOrReason = "Błędne konto docelowe.")
public class InvalidDestinationAccountException extends Exception {

    @Getter
    private String description;

}
