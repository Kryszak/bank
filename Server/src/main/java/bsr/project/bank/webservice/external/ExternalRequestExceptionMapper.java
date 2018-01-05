package bsr.project.bank.webservice.external;

import bsr.project.bank.service.validation.InvalidAmountException;
import bsr.project.bank.service.validation.InvalidSenderNameException;
import bsr.project.bank.service.validation.InvalidSourceAccountException;
import bsr.project.bank.service.validation.InvalidTitleException;
import bsr.project.bank.webservice.external.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static bsr.project.bank.model.ExternalTransferFields.*;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExternalRequestExceptionMapper {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountDoesNotExistsException.class)
    public void handleAccountDoesNotExists(AccountDoesNotExistsException e) {
        log.error("{}", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAmountException.class)
    public ValidationError handleInvalidTransferAmount(InvalidAmountException e) {
        log.error("", e);
        return ValidationError.builder().error_field(AMOUNT).error("Błędna kwota przelewu.").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTitleException.class)
    public ValidationError handleInvalidTransferTitle(InvalidTitleException e) {
        log.error("", e);
        return ValidationError
                .builder()
                .error_field(TITLE)
                .error("Błędny tytuł przelewu: " + e.getDescription())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSenderNameException.class)
    public ValidationError handleInvalidSenderName(InvalidSenderNameException e) {
        log.error("", e);
        return ValidationError.builder().error_field(NAME).error("Nie podano zlecającego przelew").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSourceAccountException.class)
    public ValidationError handleInvalidSourceAccount(InvalidSourceAccountException e) {
        log.error("", e);
        return ValidationError.builder().error_field(SOURCE_ACCOUNT).error(e.getDescription()).build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public void handleInvalidCredentials(InvalidCredentialsException e) {
        log.error("", e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ValidationError handleUnexpectedError(Exception e) {
        log.error("", e);
        return ValidationError.builder().error_field("unknown").error("Wystąpił nieoczekiwany błąd.").build();
    }
}
