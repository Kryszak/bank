package bsr.project.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTransfer {

    private Integer amount;

    private String source_account;

    private String title;

    private String name;
}
