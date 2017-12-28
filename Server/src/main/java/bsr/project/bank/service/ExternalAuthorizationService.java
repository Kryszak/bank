package bsr.project.bank.service;

import bsr.project.bank.utility.logging.LogMethodCall;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ExternalAuthorizationService {

    @LogMethodCall
    public void authenticate(String authorizationHeader) throws InvalidCredentialsException {
        if (authorizationHeader == null) {
            throw new InvalidCredentialsException();
        }
        try {
            String decoded = new String(Base64.getDecoder().decode(authorizationHeader.replace("Basic ", "")));
            String[] credentials = decoded.split(":");
            String username = credentials[0];
            String password = credentials[1];
            // TODO
        } catch (Exception ex) {
            throw new InvalidCredentialsException();
        }
    }
}
