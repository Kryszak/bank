package bsr.project.bank.webservice.external;

import bsr.project.bank.config.ExternalBankListService;
import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.utility.logging.LogMethodCall;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@Service
public class ExternalBankClient {

    @Autowired
    private ExternalBankListService bankListService;

    @Value("${app.config.externalBankUri}")
    private String uri;

    @LogMethodCall
    public void requestExternalTransfer(ExternalTransfer transfer, String destinationAccount) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String bankCode = destinationAccount.substring(2, 10);
        String bankUrl = bankListService.getBankUrl(bankCode);
        String url = bankUrl + uri.replace("{accountNumber}", destinationAccount);

        log.info("Requesting URL: {}", url);

        try {
            // TODO przepchanie usera i passów do basic auth
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    url,
                    new HttpEntity<>(transfer, createHeaders("TODO", "TODO")),
                    Void.class);

        } catch (HttpClientErrorException ex) {
            log.info("Request failed with status {}: {}", ex.getStatusCode(), ex.getMessage());
            // TODO obsługa błędu rest
        } catch (ResourceAccessException e) {
            log.info("{}", e.getMessage());
        }

    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
