package bsr.project.bank.service;

import bsr.project.bank.model.*;
import bsr.project.bank.service.data.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountOperationService {

    public static final String REMITTANCE = "WPŁATA";
    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private AccountsService accountsService;

    public List<AccountOperation> getAccountHistory(Account account) {
        return accountHistoryRepository.findByAccount(account);
    }

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
                createReceivedTransferEvent(internal, destination, (int) destinationAccountBalance));
    }

    public void internalTransfer(Transfer transfer) {
        Account sourceAccount = accountsService.getAccount(transfer.getSourceAccount());
        long sourceAccountBalance = getAndUpdateSourceAccountBalance(transfer, sourceAccount);

        Account destinationAccount = accountsService.getAccount(transfer.getDestinationAccount());
        long destinationAccountBalance = getAndUpdateDestinationAccountBalance(transfer, destinationAccount);

        saveAccountOperationEvent(
                createSentTransferEvent(transfer, sourceAccount, (int) sourceAccountBalance));
        saveAccountOperationEvent(
                createReceivedTransferEvent(transfer, destinationAccount, (int) destinationAccountBalance));
    }

    public void remittance(Payment payment) {
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
                createReceivedTransferEvent(transfer, destination, (int) destinationAccountBalance));
    }

    private AccountOperation createSentTransferEvent(Transfer transfer, Account account, int accountBalance) {
        return AccountOperation
                .builder()
                .sourceAccountNumber(transfer.getSourceAccount())
                .amount(-transfer.getAmount())
                .balance(accountBalance)
                .account(account)
                .title(transfer.getTitle())
                .build();
    }

    private AccountOperation createReceivedTransferEvent(Transfer transfer, Account account, int accountBalance) {
        return AccountOperation
                .builder()
                .sourceAccountNumber(transfer.getSourceAccount())
                .amount(-transfer.getAmount())
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

    public void externalTransfer(Transfer transfer) {
        // TODO
    }

    public void withdrawal() {
        // TODO
    }

    private void saveAccountOperationEvent(AccountOperation accountOperation) {
        accountHistoryRepository.save(accountOperation);
    }

}
