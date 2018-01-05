package bsr.project.bank.model;

import com.bsr.types.bank.ExternalTransferRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalTransfer {

    private Integer amount;

    private String source_account;

    private String title;

    private String name;

    public static ExternalTransfer fromExternalTransferRequest(ExternalTransferRequest payload){
        return builder()
                .amount(payload.getAmount())
                .name(payload.getName())
                .title(payload.getTitle())
                .source_account(payload.getSourceAccount())
                .build();
    }
}
