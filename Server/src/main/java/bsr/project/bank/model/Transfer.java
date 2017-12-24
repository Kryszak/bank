package bsr.project.bank.model;

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
}
