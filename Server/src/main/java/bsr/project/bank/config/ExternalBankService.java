package bsr.project.bank.config;

import au.com.bytecode.opencsv.CSVReader;
import bsr.project.bank.model.ExternalBankAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalBankService {

    @Value("${app.config.externalBankListFile}")
    private String filePath;

    public List<ExternalBankAddress> getAvailableBanks() throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(filePath));
        List<String[]> lines = csvReader.readAll();
        return lines
                .stream()
                .map(strings -> ExternalBankAddress.builder().bankIdNumber(strings[0]).bankUrl(strings[1]).build())
                .collect(Collectors.toList());
    }
}
