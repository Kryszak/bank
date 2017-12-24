package bsr.project.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "account_history")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    private int amount;

    private int balance;

    private String sourceAccountNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    private Account account;
}
