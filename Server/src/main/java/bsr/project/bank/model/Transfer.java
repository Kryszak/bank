package bsr.project.bank.model;

import com.bsr.types.bank.InternalTransferRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    private String sourceAccount;

    private String destinationAccount;

    private int amount;

    private String title;

    public static Transfer fromSoapRequest(InternalTransferRequest request) {
        return builder()
                .title(request.getTitle())
                .destinationAccount(request.getDestinationAccount())
                .sourceAccount(request.getSourceAccount())
                .amount(request.getAmount())
                .build();
    }
}
