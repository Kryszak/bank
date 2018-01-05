package bsr.project.bank.webservice.external;

import bsr.project.bank.config.ExternalBankListService;
import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.model.exception.AuthenticationFailedException;
import bsr.project.bank.model.exception.DestinationAccountnotFoundException;
import bsr.project.bank.model.exception.UnknownErrorException;
import bsr.project.bank.model.exception.ValidationErrorException;
import bsr.project.bank.utility.logging.LogMethodCall;
import bsr.project.bank.webservice.external.validation.ValidationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Service
public class ExternalBankClient {

    @Autowired
    private ExternalBankListService bankListService;

    @Value("${app.config.externalBankUri}")
    private String uri;

    @Value("${app.config.externalAuth.user}")
    private String authUser;

    @Value("${app.config.externalAuth.password}")
    private String authPassword;

    @Value("${app.config.client.readTimeout}")
    private int readTimeout;

    @Value("${app.config.client.connectTimeout}")
    private int connectTimeout;

    @LogMethodCall
    public void requestExternalTransfer(ExternalTransfer transfer, String destinationAccount) throws IOException, DestinationAccountnotFoundException, UnknownErrorException, AuthenticationFailedException, ValidationErrorException {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder
                .basicAuthorization(authUser, authPassword)
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();

        String bankCode = destinationAccount.substring(2, 10);
        String bankUrl = bankListService.getBankUrl(bankCode);
        String url = bankUrl + uri.replace("{accountNumber}", destinationAccount);

        log.info("Requesting URL: {}", url);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    url,
                    transfer,
                    Void.class);
            log.info("Request complete with status {}", response.getStatusCode());
        } catch (HttpClientErrorException ex) {
            HttpStatus status = ex.getStatusCode();
            log.info("Request failed with status {}: {}", status, ex.getMessage());
            if (status.equals(HttpStatus.BAD_REQUEST)) {
                ObjectMapper mapper = new ObjectMapper();
                ValidationError error = mapper.readValue(ex.getResponseBodyAsString(), ValidationError.class);
                log.info("Cause: {}", error);
                throw new ValidationErrorException(error);
            } else if (status.equals(HttpStatus.NOT_FOUND)) {
                throw new DestinationAccountnotFoundException();
            } else if (status.equals(HttpStatus.UNAUTHORIZED)) {
                throw new AuthenticationFailedException();
            } else {
                log.info("Unexpected error occured.");
                throw new UnknownErrorException();
            }
        } catch (ResourceAccessException e) {
            log.info("{}", e.getMessage());
        }

    }

}
