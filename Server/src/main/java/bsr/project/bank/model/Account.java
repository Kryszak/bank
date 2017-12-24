package bsr.project.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "accounts")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @Builder.Default
    private long balance = 0;
}
