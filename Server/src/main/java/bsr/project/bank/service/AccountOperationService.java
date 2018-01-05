package bsr.project.bank.service;

import bsr.project.bank.model.Account;
import bsr.project.bank.model.AccountOperation;
import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.model.Transfer;
import bsr.project.bank.service.data.AccountHistoryRepository;
import bsr.project.bank.utility.logging.LogMethodCall;
import bsr.project.bank.webservice.external.ExternalBankClient;
import com.bsr.types.bank.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountOperationService {

    private static final String REMITTANCE = "WPŁATA";

    private static final String WITHDRAWAL = "WYPŁATA";

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private ExternalBankClient externalBankClient;

    @LogMethodCall
    public List<AccountHistoryElement> getAccountHistory(AccountHistoryRequest payload) {
        Account account = accountsService.getAccount(payload.getAccount());
        return getAccountHistory(account)
                .stream()
                .map(operation -> {
                    AccountHistoryElement element = new AccountHistoryElement();
                    element.setAmount(operation.getAmount());
                    element.setBalance(operation.getBalance());
                    element.setSourceAccountNumber(operation.getSourceAccountNumber());
                    element.setTitle(operation.getTitle());
                    return element;
                })
                .collect(Collectors.toList());
    }

    public List<AccountOperation> getAccountHistory(Account account) {
        return accountHistoryRepository.findByAccount(account);
    }

    @LogMethodCall
    public void externalTransfer(ExternalTransfer transfer, String destinationAccount) throws IOException {
        Account sourceAccount = accountsService.getAccount(transfer.getSource_account());
        externalBankClient.requestExternalTransfer(transfer, destinationAccount);
        Transfer internal = Transfer
                .builder()
                .amount(transfer.getAmount())
                .destinationAccount(destinationAccount)
                .sourceAccount(transfer.getSource_account())
                .title(transfer.getTitle())
                .build();
        long sourceAccountBalance = getAndUpdateSourceAccountBalance(internal, sourceAccount);
        saveAccountOperationEvent(
                createLossEvent(internal, sourceAccount, (int) sourceAccountBalance));
    }

    @LogMethodCall
    public void transfer(ExternalTransfer transfer, String destinationAccount) {
        Account destination = accountsService.getAccount(destinationAccount);
        Transfer internal = Transfer
                .builder()
                .amount(transfer.getAmount())
                .destinationAccount(destinationAccount)
                .sourceAccount(transfer.getSource_account())
                .title(transfer.getTitle())
                .build();
        long destinationAccountBalance = getAndUpdateDestinationAccountBalance(internal, destination);
        saveAccountOperationEvent(
                createIncomeEvent(internal, destination, (int) destinationAccountBalance));
    }

    @LogMethodCall
    public void internalTransfer(InternalTransferRequest payload) {
        Transfer transfer = Transfer.fromSoapRequest(payload);
        Account sourceAccount = accountsService.getAccount(transfer.getSourceAccount());
        Account destinationAccount = accountsService.getAccount(transfer.getDestinationAccount());

        long sourceAccountBalance = getAndUpdateSourceAccountBalance(transfer, sourceAccount);
        long destinationAccountBalance = getAndUpdateDestinationAccountBalance(transfer, destinationAccount);

        saveAccountOperationEvent(
                createLossEvent(transfer, sourceAccount, (int) sourceAccountBalance));
        saveAccountOperationEvent(
                createIncomeEvent(transfer, destinationAccount, (int) destinationAccountBalance));
    }

    @LogMethodCall
    public void remittance(PaymentRequest payment) {
        Account destination = accountsService.getAccount(payment.getDestinationAccount());
        Transfer transfer = Transfer
                .builder()
                .amount(payment.getAmount())
                .destinationAccount(destination.getAccountNumber())
                .sourceAccount(destination.getAccountNumber())
                .title(REMITTANCE)
                .build();
        long destinationAccountBalance = getAndUpdateDestinationAccountBalance(transfer, destination);
        saveAccountOperationEvent(
                createIncomeEvent(transfer, destination, (int) destinationAccountBalance));
    }

    @LogMethodCall
    public void withdrawal(WithdrawalRequest payment) {
        Account destination = accountsService.getAccount(payment.getDestinationAccount());
        Transfer transfer = Transfer
                .builder()
                .amount(payment.getAmount())
                .destinationAccount(destination.getAccountNumber())
                .sourceAccount(destination.getAccountNumber())
                .title(WITHDRAWAL)
                .build();
        long accountBalance = getAndUpdateSourceAccountBalance(transfer, destination);
        saveAccountOperationEvent(
                createLossEvent(transfer, destination, (int) accountBalance));
    }

    private AccountOperation createLossEvent(Transfer transfer, Account account, int accountBalance) {
        return AccountOperation
                .builder()
                .sourceAccountNumber(transfer.getSourceAccount())
                .amount(-transfer.getAmount())
                .balance(accountBalance)
                .account(account)
                .title(transfer.getTitle())
                .build();
    }

    private AccountOperation createIncomeEvent(Transfer transfer, Account account, int accountBalance) {
        return AccountOperation
                .builder()
                .sourceAccountNumber(transfer.getSourceAccount())
                .amount(transfer.getAmount())
                .balance(accountBalance)
                .account(account)
                .title(transfer.getTitle())
                .build();
    }

    private long getAndUpdateDestinationAccountBalance(Transfer transfer, Account destinationAccount) {
        long destinationAccountBalance = destinationAccount.getBalance();
        destinationAccountBalance += transfer.getAmount();
        destinationAccount.setBalance(destinationAccountBalance);
        accountsService.updateAccount(destinationAccount);
        return destinationAccountBalance;
    }

    private long getAndUpdateSourceAccountBalance(Transfer transfer, Account sourceAccount) {
        long sourceAccountBalance = sourceAccount.getBalance();
        sourceAccountBalance -= transfer.getAmount();
        sourceAccount.setBalance(sourceAccountBalance);
        accountsService.updateAccount(sourceAccount);
        return sourceAccountBalance;
    }

    private void saveAccountOperationEvent(AccountOperation accountOperation) {
        accountHistoryRepository.save(accountOperation);
    }

}
