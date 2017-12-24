package bsr.project.bank.utility;

import bsr.project.bank.model.User;
import bsr.project.bank.service.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AccountController {

    @Autowired
    private AccountsService accountsService;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody User user) {
        log.info(">>> createAccount for {}", user);
        accountsService.createAccount(user);
        log.info("<<< createAccount");
    }
}
