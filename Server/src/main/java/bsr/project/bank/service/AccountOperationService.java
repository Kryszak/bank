package bsr.project.bank.service;

import bsr.project.bank.model.Account;
import bsr.project.bank.model.AccountOperation;
import bsr.project.bank.model.ExternalTransfer;
import bsr.project.bank.model.Transfer;
import bsr.project.bank.service.data.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountOperationService {

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    @Autowired
    private AccountsService accountsService;

    public List<AccountOperation> getAccountHistory(Account account) {
        return accountHistoryRepository.findByAccount(account);
    }

    public void transfer(ExternalTransfer transfer, String destinationAccount) {
        // TODO
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

    public void remittance() {
        // TODO
    }

    public void withdrawal() {
        // TODO
    }

    private void saveAccountOperationEvent(AccountOperation accountOperation) {
        accountHistoryRepository.save(accountOperation);
    }

}
