package bsr.project.bank.model.exception;

import bsr.project.bank.webservice.external.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorException extends Throwable {

    @Getter
    private ValidationError validationError;
}
