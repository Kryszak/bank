package bsr.project.bank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
public class NrbService {

    private static final String DIGITS = "0123456789";

    private static final String countryCode = "2521";

    private Random rng = new SecureRandom();

    @Value("${app.config.bankCode}")
    private String bankCode;

    private char randomChar() {
        return DIGITS.charAt(rng.nextInt(DIGITS.length()));
    }

    public String generateNrb() {
        StringBuilder sb = new StringBuilder();
        int length = 16;
        while (length > 0) {
            length--;
            sb.append(randomChar());
        }
        String initial = bankCode + sb.toString() + countryCode + "00";
        int crc = 0;
        for (int i = 0; i < initial.length(); i++) {
            crc = (10 * crc + Integer.parseInt("" + initial.charAt(i))) % 97;
        }
        crc = 98 - crc;
        return String.format("%02d", crc) + bankCode + sb.toString();
    }
}
