package bsr.project.bank.service;

import bsr.project.bank.model.Account;
import bsr.project.bank.model.AccountOperation;
import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.model.User;
import bsr.project.bank.model.exception.AuthenticationFailedException;
import bsr.project.bank.model.exception.DestinationAccountNotFoundException;
import bsr.project.bank.model.exception.UnknownErrorException;
import bsr.project.bank.model.exception.ValidationErrorException;
import bsr.project.bank.webservice.external.ExternalBankClient;
import com.bsr.types.bank.InternalTransferRequest;
import com.bsr.types.bank.ObjectFactory;
import com.bsr.types.bank.PaymentRequest;
import com.bsr.types.bank.WithdrawalRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
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

    @MockBean
    private ExternalBankClient externalBankClient;

    private static final int TRANSFER_AMOUNT = 100;

    private ObjectFactory objectFactory = new ObjectFactory();

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

        InternalTransferRequest payload = objectFactory.createInternalTransferRequest();
        payload.setAmount(TRANSFER_AMOUNT);
        payload.setSourceAccount(source.getAccountNumber());
        payload.setDestinationAccount(destination.getAccountNumber());
        payload.setTitle("test transfer");

        // when
        accountOperationService.internalTransfer(payload);

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

    @Test
    public void shouldHandleIncomingExternalTransfer() {
        // given
        User user = User.builder().name("test").password("test").build();
        userService.createUser(user);
        Account destination = accountsService.createAccount(user);
        int historySize = accountOperationService.getAccountHistory(destination).size();
        int balance = (int) destination.getBalance();
        ExternalTransfer transfer = ExternalTransfer
                .builder()
                .amount(TRANSFER_AMOUNT)
                .name("test name")
                .source_account(destination.getAccountNumber())
                .title("test title")
                .build();

        // when
        accountOperationService.transfer(transfer, destination.getAccountNumber());

        // then
        destination = accountsService.getAccount(destination.getAccountNumber());
        Assertions.assertThat(accountOperationService.getAccountHistory(destination).size() - historySize).isEqualTo(1);
        Assertions.assertThat(destination.getBalance()).isEqualTo(balance + TRANSFER_AMOUNT);
    }

    @Test
    public void shouldHandleRemittance() {
        // given
        User user = User.builder().name("test").password("test").build();
        userService.createUser(user);
        Account destination = accountsService.createAccount(user);
        int historySize = accountOperationService.getAccountHistory(destination).size();
        int balance = (int) destination.getBalance();
        PaymentRequest paymentRequest = objectFactory.createPaymentRequest();
        paymentRequest.setAmount(TRANSFER_AMOUNT);
        paymentRequest.setDestinationAccount(destination.getAccountNumber());

        // when
        accountOperationService.remittance(paymentRequest);

        // then
        destination = accountsService.getAccount(destination.getAccountNumber());
        Assertions.assertThat(accountOperationService.getAccountHistory(destination).size() - historySize).isEqualTo(1);
        Assertions.assertThat(destination.getBalance()).isEqualTo(balance + TRANSFER_AMOUNT);
    }

    @Test
    public void shouldHandleWithdrawal() {
        // given
        User user = User.builder().name("test").password("test").build();
        userService.createUser(user);
        Account destination = accountsService.createAccount(user);
        int historySize = accountOperationService.getAccountHistory(destination).size();
        int balance = (int) destination.getBalance();
        WithdrawalRequest paymentRequest = objectFactory.createWithdrawalRequest();
        paymentRequest.setDestinationAccount(destination.getAccountNumber());
        paymentRequest.setAmount(TRANSFER_AMOUNT);

        // when
        accountOperationService.withdrawal(paymentRequest);

        // then
        destination = accountsService.getAccount(destination.getAccountNumber());
        Assertions.assertThat(accountOperationService.getAccountHistory(destination).size() - historySize).isEqualTo(1);
        Assertions.assertThat(destination.getBalance()).isEqualTo(balance - TRANSFER_AMOUNT);
    }

    @Test
    public void shouldHandleExternalTransfer() throws IOException,
            AuthenticationFailedException,
            DestinationAccountNotFoundException,
            UnknownErrorException,
            ValidationErrorException {
        // given
        User user = User.builder().name("test").password("test").build();
        userService.createUser(user);
        Account source = accountsService.createAccount(user);
        int historySize = accountOperationService.getAccountHistory(source).size();
        int balance = (int) source.getBalance();
        ExternalTransfer transfer = ExternalTransfer
                .builder()
                .title("test title")
                .source_account(source.getAccountNumber())
                .name("test name")
                .amount(TRANSFER_AMOUNT)
                .build();

        // when
        accountOperationService.externalTransfer(transfer, source.getAccountNumber());

        // then
        source = accountsService.getAccount(source.getAccountNumber());
        Assertions.assertThat(accountOperationService.getAccountHistory(source).size() - historySize).isEqualTo(1);
        Assertions.assertThat(source.getBalance()).isEqualTo(balance - TRANSFER_AMOUNT);
    }
}