package bsr.project.bank.service;

import bsr.project.bank.model.Account;
import bsr.project.bank.model.AccountOperation;
import bsr.project.bank.model.Transfer;
import bsr.project.bank.model.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountOperationServiceTest {

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountOperationService accountOperationService;

    private static final int TRANSFER_AMOUNT = 100;

    @Test
    public void shouldHandleInternalTransfer() {
        // given
        User user = User.builder().name("test").password("test").build();
        userService.createUser(user);
        Account source = accountsService.createAccount(user);
        Account destination = accountsService.createAccount(user);
        source.setBalance(TRANSFER_AMOUNT);
        accountsService.updateAccount(source);
        destination.setBalance(1000);
        accountsService.updateAccount(destination);
        Transfer transfer = Transfer
                .builder()
                .title("test transfer")
                .sourceAccount(source.getAccountNumber())
                .destinationAccount(destination.getAccountNumber())
                .amount(TRANSFER_AMOUNT)
                .build();

        // when
        accountOperationService.internalTransfer(transfer);

        //then
        List<AccountOperation> sourceHistory = accountOperationService.getAccountHistory(source);
        List<AccountOperation> destinationHistory = accountOperationService.getAccountHistory(destination);
        Assertions.assertThat(sourceHistory.size()).isEqualTo(1);
        Assertions.assertThat(destinationHistory.size()).isEqualTo(1);
        source = accountsService.getAccount(source.getAccountNumber());
        destination = accountsService.getAccount(destination.getAccountNumber());
        Assertions.assertThat(source.getBalance()).isEqualTo(0);
        Assertions.assertThat(destination.getBalance()).isEqualTo(1100);
    }
}