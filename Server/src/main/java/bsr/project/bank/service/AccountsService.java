package bsr.project.bank.service;

import bsr.project.bank.service.data.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

    @Autowired
    private AccountRepository accountRepository;

    public boolean accountExists(String accountNumber) {
        return accountRepository.exists(accountNumber);
    }


}

