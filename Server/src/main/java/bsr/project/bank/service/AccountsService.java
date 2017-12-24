package bsr.project.bank.service;

import bsr.project.bank.model.Account;
import bsr.project.bank.model.User;
import bsr.project.bank.service.data.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NrbService nrbService;

    public boolean accountExists(String accountNumber) {
        return accountRepository.exists(accountNumber);
    }

    public void createAccount(User user) {
        if (!userService.userExists(user)) {
            log.warn("User does not exists, skipping...");
        }
        List<String> nrbs = accountRepository.findAll().stream().map(Account::getAccountNumber).collect(Collectors.toList());
        String nrb = nrbService.generateNrb();
        while (nrbs.contains(nrb)) {
            nrb = nrbService.generateNrb();
        }
        log.info("Creating account for user {} with NRB: {}", user, nrb);
        accountRepository.save(Account
                .builder()
                .user(user)
                .accountNumber(nrb)
                .build());
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}

